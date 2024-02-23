package me.outspending.core.mining.shapes

import me.outspending.core.mining.PacketShape
import me.outspending.core.misc.WeightedCollection
import me.outspending.core.pmines.PrivateMine
import me.outspending.core.pmines.sync.PacketSync
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.data.BlockData
import org.bukkit.util.Vector
import org.jetbrains.annotations.ApiStatus.Experimental

@Experimental
class CuboidShape(private val minLocation: Location, private val maxLocation: Location) : PacketShape() {

    override fun process(mine: PrivateMine, blockLocation: Location?, blockData: BlockData): Int = process(mine, blockLocation, blockData) { _, _ -> }

    override fun process(
        mine: PrivateMine,
        blockLocation: Location?,
        blockData: BlockData,
        perBlock: (Location, BlockData) -> Unit
    ): Int {
        val mineBlocks: Set<Location> = mine.getMine().getBlocks().keys.toSet() // Using set because its O(1) for .contains()
        val (blocksChanged, blockDataMap) = runBetween(super.MINE_WORLD, minLocation, maxLocation) {
            if (mineBlocks.contains(it)) {
                perBlock(it, blockData)
                blockData
            } else {
                null
            }
        }
        val keys: List<Location> = blockDataMap.keys.toList()

        PacketSync.syncBlocks(mine, blockDataMap)
        updateBlocks(mine.getMine(), keys)
        return blocksChanged
    }

    override fun process(
        mine: PrivateMine,
        blockLocation: Location?, // Cuboid doesn't need a block location therefore can be null
        weightedBlockData: WeightedCollection<BlockData>
    ): Int = process(mine, blockLocation, weightedBlockData) { _, _ -> }

    override fun process(
        mine: PrivateMine,
        blockLocation: Location?,
        weightedBlockData: WeightedCollection<BlockData>,
        perBlock: (Location, BlockData) -> Unit
    ): Int {
        val mineBlocks: Set<Location> = mine.getMine().getBlocks().keys.toSet() // Using set because its O(1) for .contains()
        val (blocksChanged, blockDataMap) = runBetween(super.MINE_WORLD, minLocation, maxLocation) {
            if (mineBlocks.contains(it)) {
                val nextBlockType = weightedBlockData.next()

                perBlock(it, nextBlockType)
                nextBlockType
            } else {
                null
            }
        }
        val keys: List<Location> = blockDataMap.keys.toList()

        PacketSync.syncBlocks(mine, blockDataMap)
        updateBlocks(mine.getMine(), keys)
        return blocksChanged
    }
}
