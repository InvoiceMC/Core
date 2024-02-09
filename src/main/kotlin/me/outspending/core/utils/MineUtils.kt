package me.outspending.core.utils

import me.outspending.core.packets.sync.PacketSync
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.data.BlockData
import org.bukkit.entity.Player

object MineUtils {

    private val NULLBLOCK = Material.AIR.createBlockData()

    private fun setBlock(
        player: Player,
        blockLocation: Location,
        blocks: MutableMap<Location, BlockData>,
        syncPackets: Boolean
    ) {
        if (syncPackets) {
            PacketSync.syncBlocks(blockLocation, blocks)
            return
        }

        player.sendMultiBlockChange(blocks)
    }

    /**
     * Sets the client blocks using a shape.
     *
     * If syncPackets is true, it will send the blocks to the client using a list, which will sync
     * between clients If syncPackets is false, it will send the blocks to the client using a
     * packet, which will not sync between clients
     *
     * @return the number of blocks changed
     */
    fun setBlocks(
        player: Player,
        blockLocation: Location,
        shape: Shape,
        blockData: BlockData = NULLBLOCK,
        syncPackets: Boolean = false
    ): Int {
        val (num, blocks) = shape.run(blockLocation, blockData)
        setBlock(player, blockLocation, blocks, syncPackets)

        return num
    }

    fun setBlocks(
        player: Player,
        blockLocation: Location,
        shape: Shape,
        weightedCollection: WeightedCollection<BlockData>,
        syncPackets: Boolean = false
    ): Int {
        val (num, blocks) = shape.run(blockLocation, weightedCollection)
        setBlock(player, blockLocation, blocks, syncPackets)

        return num
    }
}
