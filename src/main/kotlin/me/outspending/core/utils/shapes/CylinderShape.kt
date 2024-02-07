package me.outspending.core.utils.shapes

import me.outspending.core.utils.Shape
import me.outspending.core.utils.Utilities.toLocation
import me.outspending.core.utils.runXYZ
import org.bukkit.Location
import org.bukkit.block.data.BlockData
import org.bukkit.util.Vector

class CylinderShape(private val radius: Int, private val height: Int) : Shape {

    override fun run(blockLocation: Location, blockData: BlockData): Pair<Int, MutableMap<Location, BlockData>> {
        val vec1 = Vector(radius, height, radius)
        val vec2 = Vector(-radius, -height, -radius)

        val world = blockLocation.world
        val blockVector = Shape.BlockVector3D(blockLocation, vec1, vec2)

        var num = 0
        val blocks = runXYZ(blockVector) { x, y, z, blockChanges ->
            val location = toLocation(world, x, y, z)
            val distance = blockLocation.distance(location)

            if (distance <= radius) {
                blockChanges[location] = blockData

                num++
            }
        }

        return (num to blocks)
    }
}