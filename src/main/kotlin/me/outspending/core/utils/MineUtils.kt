package me.outspending.core.utils

import me.outspending.core.packets.sync.PacketSync
import me.outspending.core.utils.Utilities.getConnection
import me.outspending.core.utils.Utilities.toLocation
import net.minecraft.core.BlockPos
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket
import net.minecraft.server.network.ServerGamePacketListenerImpl
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.block.data.BlockData
import org.bukkit.entity.Player
import org.bukkit.util.Vector
import kotlin.math.max
import kotlin.math.min

object MineUtils {

    private val NULLBLOCK: BlockData = Material.AIR.createBlockData()

    fun setBlocksXYZ(
        player: Player,
        blockLocation: Location,
        vec1: Vector,
        vec2: Vector,
        blockData: BlockData = NULLBLOCK,
        syncPackets: Boolean = false
    ): Int {
        if (syncPackets) {
            return PacketSync.syncBlocksXYZ(blockLocation, vec1, vec2, blockData)
        } else {
            val (minX, minY, minZ, maxX, maxY, maxZ) = BlockVector3D(blockLocation, vec1, vec2)
            val world = blockLocation.world
            val updateBlocks: MutableMap<Location, BlockData> = mutableMapOf()

            var num = 0
            for (x in minX..maxX) {
                for (y in minY..maxY) {
                    for (z in minZ..maxZ) {
                        val location = toLocation(world, x, y, z)
                        updateBlocks[location] = blockData

                        num++
                    }
                }
            }

            player.sendMultiBlockChange(updateBlocks)
            return num
        }
    }

    fun setBlocksXZ(
        player: Player,
        blockLocation: Location,
        vec1: Vector,
        vec2: Vector,
        blockData: BlockData = NULLBLOCK,
        syncPackets: Boolean = false,
    ): Int {
        if (syncPackets) {
            return PacketSync.syncBlocksXZ(blockLocation, vec1, vec2, blockData)
        } else {
            val (minX, minY, minZ, maxX, _, maxZ) = BlockVector3D(blockLocation, vec1, vec2)
            val world = blockLocation.world
            val updateBlocks: MutableMap<Location, BlockData> = mutableMapOf()

            var num = 0
            for (x in minX..maxX) {
                for (z in minZ..maxZ) {
                    val location = toLocation(world, x, minY, z)
                    updateBlocks[location] = blockData

                    num++
                }
            }

            player.sendMultiBlockChange(updateBlocks)
            return num
        }
    }

    fun setBlocksSphere(
        player: Player,
        blockLocation: Location,
        radius: Int,
        blockData: BlockData = NULLBLOCK,
        syncPackets: Boolean = false
    ): Int {
        if (syncPackets) {
            return PacketSync.syncBlocksSphere(blockLocation, radius, blockData)
        } else {
            val vec1 = Vector(radius, radius, radius)
            val vec2 = Vector(-radius, -radius, -radius)

            val (minX, minY, minZ, maxX, maxY, maxZ) = BlockVector3D(blockLocation, vec1, vec2)
            val world = blockLocation.world
            val updateBlocks: MutableMap<Location, BlockData> = mutableMapOf()

            var num = 0
            for (x in minX..maxX) {
                for (y in minY..maxY) {
                    for (z in minZ..maxZ) {
                        val location = toLocation(world, x, y, z)
                        val distance = blockLocation.distance(location)

                        if (distance <= radius) {
                            updateBlocks[location] = blockData
                            num++
                        }
                    }
                }
            }

            player.sendMultiBlockChange(updateBlocks)
            return num
        }
    }

    fun setBlocksCylinder(
        player: Player,
        blockLocation: Location,
        radius: Int,
        height: Int,
        blockData: BlockData = NULLBLOCK,
        syncPackets: Boolean = false
    ): Int {
        if (syncPackets) {
            return PacketSync.syncBlocksCylinder(blockLocation, radius, height, blockData)
        } else {
            val vec1 = Vector(radius, height, radius)
            val vec2 = Vector(-radius, -height, -radius)

            val (minX, minY, minZ, maxX, maxY, maxZ) = BlockVector3D(blockLocation, vec1, vec2)
            val world = blockLocation.world
            val updateBlocks: MutableMap<Location, BlockData> = mutableMapOf()

            var num = 0
            for (x in minX..maxX) {
                for (y in minY..maxY) {
                    for (z in minZ..maxZ) {
                        val location = toLocation(world, x, y, z)
                        val distance = blockLocation.distance(location)

                        if (distance <= radius) {
                            updateBlocks[location] = blockData

                            num++
                        }
                    }
                }
            }

            player.sendMultiBlockChange(updateBlocks)
            return num
        }
    }

    data class BlockVector3D(
        val minX: Int,
        val minY: Int,
        val minZ: Int,
        val maxX: Int,
        val maxY: Int,
        val maxZ: Int
    ) {
        constructor(
            blockLocation: Location,
            vec1: Vector,
            vec2: Vector
        ) : this(
            min(blockLocation.blockX - vec1.blockX, blockLocation.blockX - vec2.blockX),
            min(blockLocation.blockY - vec1.blockY, blockLocation.blockY - vec2.blockY),
            min(blockLocation.blockZ - vec1.blockZ, blockLocation.blockZ - vec2.blockZ),
            max(blockLocation.blockX - vec1.blockX, blockLocation.blockX - vec2.blockX),
            max(blockLocation.blockY - vec1.blockY, blockLocation.blockY - vec2.blockY),
            max(blockLocation.blockZ - vec1.blockZ, blockLocation.blockZ - vec2.blockZ)
        )
    }
}
