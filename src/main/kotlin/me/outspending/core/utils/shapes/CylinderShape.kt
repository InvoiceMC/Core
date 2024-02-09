package me.outspending.core.utils.shapes

import me.outspending.core.utils.Shape
import me.outspending.core.utils.Utilities.toLocation
import me.outspending.core.utils.WeightedCollection
import me.outspending.core.utils.runXYZ
import org.bukkit.Location
import org.bukkit.block.data.BlockData
import org.bukkit.util.Vector

class CylinderShape(private val radius: Int, private val height: Int) : Shape {
    private val vec1 = Vector(radius, height, radius)
    private val vec2 = Vector(-radius, -height, -radius)

    override fun run(blockLocation: Location, blockData: BlockData): Pair<Int, MutableMap<Location, BlockData>> {
        return runInternal(blockLocation, vec1, vec2) { location, blockChanges ->
            val distance = blockLocation.distance(location)
            val y = location.y - blockLocation.y

            if (distance <= radius && y <= height) {
                blockChanges[location] = blockData
            }
        }
    }

    override fun run(
        blockLocation: Location,
        weightedCollection: WeightedCollection<BlockData>
    ): Pair<Int, MutableMap<Location, BlockData>> {
        return runInternal(blockLocation, vec1, vec2) { location, blockChanges ->
            val distance = blockLocation.distance(location)
            val y = location.y - blockLocation.y

            if (distance <= radius && y <= height) {
                blockChanges[location] = weightedCollection.next()
            }
        }
    }
}