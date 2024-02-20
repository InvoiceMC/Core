package me.outspending.core.mining.shapes

import me.outspending.core.mining.Shape
import me.outspending.core.misc.WeightedCollection
import org.bukkit.Location
import org.bukkit.block.data.BlockData
import org.bukkit.util.BoundingBox
import org.bukkit.util.Vector

class CylinderShape(private val radius: Int, private val height: Int) : Shape {
    private val vec1 = Vector(radius, height, radius)
    private val vec2 = Vector(-radius, -height, -radius)

    override fun run(
        region: BoundingBox,
        blockLocation: Location,
        blockData: BlockData
    ): Pair<Int, MutableMap<Location, BlockData>> =
        runInternal(region, blockLocation, vec1, vec2) { location, blockChanges ->
            val distance = blockLocation.distance(location)
            val y = location.y - blockLocation.y

            if (distance <= radius && y <= height) {
                blockChanges[location] = blockData
            }
        }

    override fun run(
        region: BoundingBox,
        blockLocation: Location,
        weightedCollection: WeightedCollection<BlockData>
    ): Pair<Int, MutableMap<Location, BlockData>> =
        runInternal(region, blockLocation, vec1, vec2) { location, blockChanges ->
            val distance = blockLocation.distance(location)
            val y = location.y - blockLocation.y

            if (distance <= radius && y <= height) {
                blockChanges[location] = weightedCollection.next()
            }
        }
}
