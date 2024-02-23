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

class SphereShape(private val radius: Int) : PacketShape() {
    private val maxVector = Vector(radius, radius, radius)
    private val minVector = Vector(-radius, -radius, -radius)

    override fun process(mine: PrivateMine, blockLocation: Location?, blockData: BlockData): Int {
        requireNotNull(blockLocation) { "Block location cannot be null for SphereShape" }

        val (blocksChanged, blockDataMap) = runBetween(super.MINE_WORLD, minVector, maxVector) { location ->
            println("$location // $blockLocation")
            val distance = blockLocation.distance(location)

            // TODO: DEBUG
            println("$distance // $radius")
            if (distance <= radius) blockData
            else null
        }
        val keys: List<Location> = blockDataMap.keys.toList()

        // TODO: DEBUG
        println(blocksChanged)
        println(blockDataMap)

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

        val (blocksChanged, blockDataMap) = runBetween(super.MINE_WORLD, minVector, maxVector) { location ->
            val distance = blockLocation.distance(location)


            if (distance <= radius) weightedBlockData.next()
            else null
        }
        val keys: List<Location> = blockDataMap.keys.toList()

        PacketSync.syncBlocks(mine, blockDataMap)
        updateBlocks(mine.getMine(), keys)
        return blocksChanged
    }
}
