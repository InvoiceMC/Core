package me.outspending.core.mining

import me.outspending.core.mining.shapes.CuboidShape
import me.outspending.core.misc.WeightedCollection
import me.outspending.core.pmines.Extensions.getPmine
import me.outspending.core.pmines.Mine
import me.outspending.core.pmines.sync.PacketSync
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.data.BlockData
import org.bukkit.entity.Player
import org.bukkit.util.BoundingBox

object MineUtils {

    private val NULLBLOCK = Material.AIR.createBlockData()

    private fun setBlock(
        player: Player,
        blockLocation: Location,
        blocks: MutableMap<Location, BlockData>,
        syncPackets: Boolean
    ) {
        val pmine = player.getPmine()
        if (syncPackets) {
            PacketSync.syncBlocks(pmine, blockLocation, blocks)
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
        region: BoundingBox,
        blockLocation: Location,
        shape: Shape,
        blockData: BlockData = NULLBLOCK,
        syncPackets: Boolean = false
    ): Int {
        val (num, blocks) = shape.run(region, blockLocation, blockData)
        setBlock(player, blockLocation, blocks, syncPackets)

        return num
    }

    fun setBlocks(
        player: Player,
        region: BoundingBox,
        blockLocation: Location,
        shape: Shape,
        weightedCollection: WeightedCollection<BlockData>,
        syncPackets: Boolean = false
    ): Int {
        val (num, blocks) = shape.run(region, blockLocation, weightedCollection)
        setBlock(player, blockLocation, blocks, syncPackets)

        return num
    }
}
