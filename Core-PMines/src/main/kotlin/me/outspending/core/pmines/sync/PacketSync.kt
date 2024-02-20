package me.outspending.core.pmines.sync

import me.outspending.core.pmines.PrivateMine
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.block.data.BlockData
import org.bukkit.entity.Player

object PacketSync {

    private val RENDER_DISTANCE: Int = Bukkit.getViewDistance() * 16

    /** Gets the players that can see the location */
    private fun getPminePlayers(pmine: PrivateMine, location: Location): List<Player> =
        pmine.getAllMembers()
            .filter { location.distance(it.location) <= RENDER_DISTANCE } // Which will be changed to world when finished
            .toList()

    /**
     * Sends the block changes to the player using a list, which is faster cuz its using
     * ChunkSectionChanges
     */
    private fun sendChanges(
        players: List<Player>,
        changes: MutableMap<Location, BlockData>
    ) = players.forEach { it.sendMultiBlockChange(changes) }

    /** Syncs the block between clients that can physically see the location */
    fun syncBlock(pmine: PrivateMine, blockLocation: Location, blockData: BlockData) =
        getPminePlayers(pmine, blockLocation).forEach { it.sendBlockChange(blockLocation, blockData) }

    /**
     * Syncs the blocks between clients that can physically see the location
     *
     * @return the number of blocks changed
     */
    fun syncBlocks(pmine: PrivateMine, blockLocation: Location, blocks: MutableMap<Location, BlockData>) =
        sendChanges(getPminePlayers(pmine, blockLocation), blocks)
}
