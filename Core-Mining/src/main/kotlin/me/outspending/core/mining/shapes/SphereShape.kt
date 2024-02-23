package me.outspending.core.mining.shapes

import me.outspending.core.mining.PacketShape
import me.outspending.core.mining.Shape
import me.outspending.core.misc.WeightedCollection
import me.outspending.core.pmines.PrivateMine
import me.outspending.core.pmines.sync.PacketSync
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.data.BlockData
import org.bukkit.util.BoundingBox
import org.bukkit.util.Vector

class SphereShape(private val radius: Int) : PacketShape() {
    private val maxVector = Vector(radius, radius, radius)
    private val minVector = Vector(-radius, -radius, -radius)

    override fun process(mine: PrivateMine, blockLocation: Location?, blockData: BlockData): Int {
        requireNotNull(blockLocation) { "Block location cannot be null for SphereShape" }

        val mineBlocks: Set<Location> = mine.getMine().getBlocks().keys.toSet() // Using set because its O(1) for .contains()
        val (blocksChanged, blockDataMap) = runBetween(super.MINE_WORLD, blockLocation, minVector, maxVector) { location ->
            val distance = blockLocation.distance(location)

            if (distance <= radius && mineBlocks.contains(location)) {
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
        requireNotNull(blockLocation) { "Block location cannot be null for SphereShape" }

        val mineBlocks: Set<Location> = mine.getMine().getBlocks().keys.toSet() // Using set because its O(1) for .contains()
        val (blocksChanged, blockDataMap) = runBetween(super.MINE_WORLD, blockLocation, minVector, maxVector) { location ->
            val distance = blockLocation.distance(location)

            if (distance <= radius && mineBlocks.contains(location)) {
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
