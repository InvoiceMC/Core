package me.outspending.core.mining

import me.outspending.core.misc.WeightedCollection
import me.outspending.core.pmines.PrivateMine
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.data.BlockData

object MineUtils {

    private val NULLBLOCK = Material.AIR.createBlockData()

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
        mine: PrivateMine,
        blockLocation: Location,
        shape: PacketShape,
        blockData: BlockData = NULLBLOCK,
    ): Int = shape.process(mine, blockLocation, blockData)

    fun setBlocks(
        mine: PrivateMine,
        blockLocation: Location,
        shape: PacketShape,
        weightedCollection: WeightedCollection<BlockData>,
    ): Int = shape.process(mine, blockLocation, weightedCollection)
}
