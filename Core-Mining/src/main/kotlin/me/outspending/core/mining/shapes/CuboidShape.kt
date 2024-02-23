package me.outspending.core.mining.shapes

import me.outspending.core.mining.PacketShape
import me.outspending.core.misc.WeightedCollection
import me.outspending.core.pmines.PrivateMine
import me.outspending.core.pmines.sync.PacketSync
import org.bukkit.Location
import org.bukkit.block.data.BlockData
import org.bukkit.util.Vector
import org.jetbrains.annotations.ApiStatus.Experimental

@Experimental
class CuboidShape(private val minVec: Vector, private val maxVec: Vector) : PacketShape() {
    override fun process(mine: PrivateMine, blockLocation: Location?, blockData: BlockData): Int {
        val (blocksChanged, blockDataMap) = runBetween(super.MINE_WORLD, minVec, maxVec) { blockData }
        val keys: List<Location> = blockDataMap.keys.toList()

        PacketSync.syncBlocks(mine, blockDataMap)
        updateBlocks(mine.getMine(), keys)
        return blocksChanged
    }

    override fun process(
        mine: PrivateMine,
        blockLocation: Location?, // Cuboid doesn't need a block location therefore can be null
        weightedBlockData: WeightedCollection<BlockData>
    ): Int {
        val (blocksChanged, blockData) = runBetween(super.MINE_WORLD, minVec, maxVec) { weightedBlockData.next() }

        PacketSync.syncBlocks(mine, blockData)
        updateBlocks(mine.getMine(), blockData.keys.toList())
        return blocksChanged
    }
}
