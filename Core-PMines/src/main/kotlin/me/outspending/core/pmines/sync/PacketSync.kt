package me.outspending.core.pmines.sync

import me.outspending.core.pmines.PrivateMine
import org.bukkit.Location
import org.bukkit.block.data.BlockData
import org.bukkit.entity.Player

object PacketSync {
    /** Gets the players that can see the location */
    private fun getPminePlayers(pmine: PrivateMine): List<Player> = pmine.getAllOnlineMembers()

    /**
     * Sends the block changes to the player using a list, which is faster cuz its using
     * ChunkSectionChanges
     */
    private fun sendChanges(
        players: List<Player>,
        changes: Map<Location, BlockData>
    ) = players.forEach { it.sendMultiBlockChange(changes) }

    /** Syncs the block between clients that can physically see the location */
    fun syncBlock(pmine: PrivateMine, blockLocation: Location, blockData: BlockData) =
        getPminePlayers(pmine).forEach { it.sendBlockChange(blockLocation, blockData) }

    /**
     * Syncs the blocks between clients that can physically see the location
     *
     * @return the number of blocks changed
     */
    fun syncBlocks(pmine: PrivateMine, blocks: Map<Location, BlockData>) =
        sendChanges(getPminePlayers(pmine), blocks)
}