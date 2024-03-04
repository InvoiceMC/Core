package me.outspending.core.mining.shapes

import me.outspending.core.mining.PacketShape
import me.outspending.core.misc.WeightedCollection
import me.outspending.core.pmines.PrivateMine
import me.outspending.core.pmines.sync.PacketSync
import org.bukkit.Location
import org.bukkit.block.data.BlockData
import org.bukkit.util.Vector

class CylinderShape(private val radius: Int, private val height: Int) : PacketShape() {
    private val maxVec = Vector(radius, height, radius)
    private val minVec = Vector(-radius, -height, -radius)

    override fun process(mine: PrivateMine, blockLocation: Location?, blockData: BlockData): Int = process(mine, blockLocation, blockData) { _, _ -> }

    override fun process(
        mine: PrivateMine,
        blockLocation: Location?,
        blockData: BlockData,
        perBlock: (Location, BlockData) -> Unit
    ): Int {
        requireNotNull(blockLocation) { "Block location cannot be null for CylinderShape" }

        val mineBlocks: Set<Location> = mine.getMine().getBlocks().keys
        val (blocksChanged, blockDataMap) = runBetween(super.MINE_WORLD, blockLocation, minVec, maxVec) { location ->
            val distance = blockLocation.distance(location)
            val y = location.y - blockLocation.y

            if (distance <= radius && y <= height && y >= -height && mineBlocks.contains(location)) {
                perBlock(location, blockData)
                blockData
            } else {
                null
            }
        }
        val keys: Set<Location> = blockDataMap.keys

        PacketSync.syncBlocks(mine, blockDataMap)
        updateBlocks(mine.getMine(), keys)
        return blocksChanged
    }

    override fun process(
        mine: PrivateMine,
        blockLocation: Location?,
        weightedBlockData: WeightedCollection<BlockData>
    ): Int = process(mine, blockLocation, weightedBlockData) { _, _ -> }

    override fun process(
        mine: PrivateMine,
        blockLocation: Location?,
        weightedBlockData: WeightedCollection<BlockData>,
        perBlock: (Location, BlockData) -> Unit
    ): Int {
        requireNotNull(blockLocation) { "Block location cannot be null for CylinderShape" }

        val mineBlocks: Set<Location> = mine.getMine().getBlocks().keys
        val (blocksChanged, blockDataMap) = runBetween(super.MINE_WORLD, blockLocation, minVec, maxVec) { location ->
            val distance = blockLocation.distance(location)
            val y = location.y - blockLocation.y

            if (distance <= radius && y <= height && y >= -height && mineBlocks.contains(location)) {
                val nextBlockType = weightedBlockData.next()

                perBlock(location, nextBlockType)
                nextBlockType
            } else {
                null
            }
        }
        val keys: Set<Location> = blockDataMap.keys

        PacketSync.syncBlocks(mine, blockDataMap)
        updateBlocks(mine.getMine(), keys)
        return blocksChanged
    }


}
