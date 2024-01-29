package me.outspending.core.utils

import me.outspending.core.packets.sync.PacketSync
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.data.BlockData
import org.bukkit.entity.Player

object MineUtils {

    private val NULLBLOCK = Material.AIR.createBlockData()

    /**
     * Sets the client blocks using a shape.
     *
     * If syncPackets is true, it will send the blocks to the client using a list, which will sync between clients
     * If syncPackets is false, it will send the blocks to the client using a packet, which will not sync between clients
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
        if (syncPackets) {
            return PacketSync.syncBlocks(blockLocation, shape, blockData)
        } else {
            val (num, blocks) = shape.run(blockLocation, blockData)
            player.sendMultiBlockChange(blocks)

            return num
        }
    }
}
