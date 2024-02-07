package me.outspending.core.packets.sync

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.block.data.BlockData
import org.bukkit.entity.Player

object PacketSync {

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

    /**
     * Syncs the block between clients that can physically see the location
     */
    fun syncBlock(blockLocation: Location, blockData: BlockData) =
        getSeeablePlayers(blockLocation).forEach { it.sendBlockChange(blockLocation, blockData) }

    /**
     * Syncs the blocks between clients that can physically see the location
     *
     * @return the number of blocks changed
     */
    fun syncBlocks(blockLocation: Location, blocks: MutableMap<Location, BlockData>) =
        sendChanges(getSeeablePlayers(blockLocation), blocks)
}
