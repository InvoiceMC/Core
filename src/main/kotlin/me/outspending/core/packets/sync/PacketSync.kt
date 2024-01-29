package me.outspending.core.packets.sync

import me.outspending.core.utils.MineUtils
import me.outspending.core.utils.Utilities.toLocation
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.block.data.BlockData
import org.bukkit.entity.Player
import org.bukkit.util.Vector

object PacketSync {

    private val NULLBLOCK: BlockData = Material.AIR.createBlockData()
    private val RENDER_DISTANCE: Int = Bukkit.getViewDistance() * 16

    /** Checks if the player can see the location */
    private fun getSeeablePlayers(location: Location): MutableList<Player> {
        val players = mutableListOf<Player>()

        for (player in Bukkit.getOnlinePlayers()) {
            val playerLoc: Location = player.location
            val distance: Double = location.distance(playerLoc)

            if (distance <= RENDER_DISTANCE) {
                players.add(player)
            }
        }

        return players
    }

    /**
     * Sends the block changes to the player using a list, which is faster cuz its using
     * ChunkSectionChanges
     */
    private fun sendChanges(
        players: MutableList<Player>,
        changes: MutableMap<Location, BlockData>
    ) = players.forEach { it.sendMultiBlockChange(changes) }

    /** Syncs all the packets between 2 vectors, this does update the blocks to the player!! */
    fun syncBlocksXYZ(
        blockLocation: Location,
        vec1: Vector,
        vec2: Vector,
        blockData: BlockData = NULLBLOCK,
    ): Int {
        val (minX, minY, minZ, maxX, maxY, maxZ) = MineUtils.BlockVector3D(blockLocation, vec1, vec2)
        val updateBlocks: MutableMap<Location, BlockData> = mutableMapOf()
        val world: World = blockLocation.world

        // "Setting" the blocks to the blockData
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

        // Sends the changes to all the players that can see the location, this will sync across all players
        val syncPlayers = getSeeablePlayers(blockLocation)
        sendChanges(syncPlayers, updateBlocks)

        return num
    }

    fun syncBlocksXZ(
        blockLocation: Location,
        vec1: Vector,
        vec2: Vector,
        blockData: BlockData = NULLBLOCK,
    ): Int {
        val (minX, minY, minZ, maxX, _, maxZ) = MineUtils.BlockVector3D(blockLocation, vec1, vec2)
        val updateBlocks: MutableMap<Location, BlockData> = mutableMapOf()
        val world: World = blockLocation.world

        // "Setting" the blocks to the blockData
        var num = 0
        for (x in minX..maxX) {
            for (z in minZ..maxZ) {
                val location = toLocation(world, x, minY, z)
                updateBlocks[location] = blockData

                num++
            }
        }

        // Sends the changes to all the players that can see the location, this will sync across all players
        val syncPlayers = getSeeablePlayers(blockLocation)
        sendChanges(syncPlayers, updateBlocks)

        return num
    }

    fun syncBlocksSphere(
        blockLocation: Location,
        radius: Int,
        blockData: BlockData = NULLBLOCK,
    ): Int {
        val vec1 = Vector(radius, radius, radius)
        val vec2 = Vector(-radius, -radius, -radius)

        val (minX, minY, minZ, maxX, maxY, maxZ) = MineUtils.BlockVector3D(blockLocation, vec1, vec2)
        val updateBlocks: MutableMap<Location, BlockData> = mutableMapOf()
        val world: World = blockLocation.world

        // "Setting" the blocks to the blockData
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

        // Sends the changes to all the players that can see the location, this will sync across all players
        val syncPlayers = getSeeablePlayers(blockLocation)
        sendChanges(syncPlayers, updateBlocks)

        return num
    }

    fun syncBlocksCylinder(
        blockLocation: Location,
        radius: Int,
        height: Int,
        blockData: BlockData = NULLBLOCK,
    ): Int {
        val vec1 = Vector(radius, height, radius)
        val vec2 = Vector(-radius, -height, -radius)

        val (minX, minY, minZ, maxX, maxY, maxZ) = MineUtils.BlockVector3D(blockLocation, vec1, vec2)
        val updateBlocks: MutableMap<Location, BlockData> = mutableMapOf()
        val world: World = blockLocation.world

        // "Setting" the blocks to the blockData
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

        // Sends the changes to all the players that can see the location, this will sync across all players
        val syncPlayers = getSeeablePlayers(blockLocation)
        sendChanges(syncPlayers, updateBlocks)

        return num
    }
}
