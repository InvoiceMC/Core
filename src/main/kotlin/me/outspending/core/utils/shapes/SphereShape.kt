package me.outspending.core.utils.shapes

import me.outspending.core.utils.Shape
import me.outspending.core.utils.WeightedCollection
import org.bukkit.Location
import org.bukkit.block.data.BlockData
import org.bukkit.util.Vector

class SphereShape(private val radius: Int) : Shape {
    private val vec1 = Vector(radius, radius, radius)
    private val vec2 = Vector(-radius, -radius, -radius)

    override fun run(
        blockLocation: Location,
        blockData: BlockData
    ): Pair<Int, MutableMap<Location, BlockData>> =
        runInternal(blockLocation, vec1, vec2) { location, blockChanges ->
            val distance = blockLocation.distance(location)

            if (distance <= radius) {
                blockChanges[location] = blockData
            }
        }

    override fun run(
        blockLocation: Location,
        weightedCollection: WeightedCollection<BlockData>
    ): Pair<Int, MutableMap<Location, BlockData>> =
        runInternal(blockLocation, vec1, vec2) { location, blockChanges ->
            val distance = blockLocation.distance(location)

            if (distance <= radius) {
                blockChanges[location] = weightedCollection.next()
            }
        }
}
