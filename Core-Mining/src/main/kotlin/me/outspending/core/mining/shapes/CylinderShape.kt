package me.outspending.core.mining.shapes

import me.outspending.core.mining.PacketShape
import me.outspending.core.mining.Shape
import me.outspending.core.misc.WeightedCollection
import me.outspending.core.pmines.PrivateMine
import me.outspending.core.pmines.sync.PacketSync
import org.bukkit.Location
import org.bukkit.block.data.BlockData
import org.bukkit.util.BoundingBox
import org.bukkit.util.Vector

class CylinderShape(private val radius: Int, private val height: Int) : PacketShape() {
    private val maxVec = Vector(radius, height, radius)
    private val minVec = Vector(-radius, -height, -radius)

    override fun process(mine: PrivateMine, blockLocation: Location?, blockData: BlockData): Int {
        requireNotNull(blockLocation) { "Block location cannot be null for CylinderShape" }

        val mineBlocks: Set<Location> = mine.getMine().getBlocks().keys.toSet()
        val (blocksChanged, blockDataMap) = runBetween(super.MINE_WORLD, blockLocation, minVec, maxVec) { location ->
            val distance = blockLocation.distance(location)
            val y = location.y - blockLocation.y

            if (distance <= radius && y <= height && y >= -height && mineBlocks.contains(location)) {
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
        blockLocation: Location?,
        weightedBlockData: WeightedCollection<BlockData>
    ): Int {
        requireNotNull(blockLocation) { "Block location cannot be null for CylinderShape" }

        val mineBlocks: Set<Location> = mine.getMine().getBlocks().keys.toSet()
        val (blocksChanged, blockDataMap) = runBetween(super.MINE_WORLD, blockLocation, minVec, maxVec) { location ->
            val distance = blockLocation.distance(location)
            val y = location.y - blockLocation.y

            if (distance <= radius && y <= height && y >= -height && mineBlocks.contains(location)) {
                weightedBlockData.next()
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
