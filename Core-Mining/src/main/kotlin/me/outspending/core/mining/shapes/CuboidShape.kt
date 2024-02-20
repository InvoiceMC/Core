package me.outspending.core.mining.shapes

import me.outspending.core.mining.Shape
import me.outspending.core.misc.WeightedCollection
import org.bukkit.Location
import org.bukkit.block.data.BlockData
import org.bukkit.util.BoundingBox
import org.bukkit.util.Vector

class CuboidShape(private val vec1: Vector, private val vec2: Vector) : Shape {
    constructor(loc1: Location, loc2: Location) : this(loc1.toVector(), loc2.toVector())

    override fun run(
        region: BoundingBox,
        blockLocation: Location,
        blockData: BlockData
    ): Pair<Int, MutableMap<Location, BlockData>> =
        runInternal(region, blockLocation, vec1, vec2) { location, blockChanges ->
            blockChanges[location] = blockData
        }

    override fun run(
        region: BoundingBox,
        blockLocation: Location,
        weightedCollection: WeightedCollection<BlockData>
    ): Pair<Int, MutableMap<Location, BlockData>> =
        runInternal(region, blockLocation, vec1, vec2) { location, blockChanges ->
            println("Setting block at: $location")
            blockChanges[location] = weightedCollection.next()
        }
}
