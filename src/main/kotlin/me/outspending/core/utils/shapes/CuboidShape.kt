package me.outspending.core.utils.shapes

import me.outspending.core.utils.Shape
import me.outspending.core.utils.Utilities.toLocation
import me.outspending.core.utils.WeightedCollection
import me.outspending.core.utils.runXYZ
import org.bukkit.Location
import org.bukkit.block.data.BlockData
import org.bukkit.util.Vector

class CuboidShape(private val vec1: Vector, private val vec2: Vector) : Shape {
    constructor(loc1: Location, loc2: Location) : this(loc1.toVector(), loc2.toVector())

    override fun run(blockLocation: Location, blockData: BlockData): Pair<Int, MutableMap<Location, BlockData>> {
        return runInternal(blockLocation, vec1, vec2) { location, blockChanges ->
            blockChanges[location] = blockData
        }
    }

    override fun run(
        blockLocation: Location,
        weightedCollection: WeightedCollection<BlockData>
    ): Pair<Int, MutableMap<Location, BlockData>> {
        return runInternal(blockLocation, vec1, vec2) { location, blockChanges ->
            blockChanges[location] = weightedCollection.next()
        }
    }
}