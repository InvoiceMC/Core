package me.outspending.core.utils

import me.outspending.core.utils.Utilities.getConnection
import net.minecraft.core.BlockPos
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket
import net.minecraft.server.network.ServerGamePacketListenerImpl
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.util.Vector
import kotlin.math.max
import kotlin.math.min

object MineUtils {

    private val NULLBLOCK: BlockState = Blocks.AIR.defaultBlockState()

    private fun setBlock(
        playerConnection: ServerGamePacketListenerImpl,
        position: BlockPos,
        blockState: BlockState
    ) = playerConnection.send(ClientboundBlockUpdatePacket(position, blockState))

    fun setBlocksXYZ(
        player: Player,
        blockLocation: Location,
        vec1: Vector,
        vec2: Vector,
        blockState: BlockState = NULLBLOCK
    ): Int = setBlocksXYZ(player.getConnection()!!, blockLocation, vec1, vec2, blockState)

    fun setBlocksXYZ(
        playerConnection: ServerGamePacketListenerImpl,
        blockLocation: Location,
        vec1: Vector,
        vec2: Vector,
        blockState: BlockState = NULLBLOCK
    ): Int {
        val (minX, minY, minZ, maxX, maxY, maxZ) = BlockVector3D(blockLocation, vec1, vec2)

        var num = 0
        for (x in minX..maxX) {
            for (y in minY..maxY) {
                for (z in minZ..maxZ) {
                    setBlock(playerConnection, BlockPos(x, y, z), blockState)
                    num++
                }
            }
        }
        return num
    }

    fun setBlocksXZ(
        player: Player,
        blockLocation: Location,
        vec1: Vector,
        vec2: Vector,
        blockState: BlockState = NULLBLOCK
    ): Int = setBlocksXZ(player.getConnection()!!, blockLocation, vec1, vec2, blockState)

    fun setBlocksXZ(
        playerConnection: ServerGamePacketListenerImpl,
        blockLocation: Location,
        vec1: Vector,
        vec2: Vector,
        blockState: BlockState = NULLBLOCK
    ): Int {
        val (minX, minY, minZ, maxX, _, maxZ) = BlockVector3D(blockLocation, vec1, vec2)

        var num = 0
        for (x in minX..maxX) {
            for (z in minZ..maxZ) {
                setBlock(playerConnection, BlockPos(x, minY, z), blockState)
                num++
            }
        }
        return num
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
