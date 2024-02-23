package me.outspending.core.mining

import me.outspending.core.BlockVector3D
import me.outspending.core.Utilities
import me.outspending.core.misc.WeightedCollection
import me.outspending.core.pmines.Extensions.hasLocation
import me.outspending.core.pmines.Mine
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.block.data.BlockData
import org.bukkit.util.BoundingBox
import org.bukkit.util.Vector
import kotlin.math.max
import kotlin.math.min

@Deprecated("Use PacketShape instead")
interface Shape {

    fun run(
        region: BoundingBox,
        blockLocation: Location,
        blockData: BlockData
    ): Pair<Int, MutableMap<Location, BlockData>>

    fun run(
        region: BoundingBox,
        blockLocation: Location,
        weightedCollection: WeightedCollection<BlockData>
    ): Pair<Int, MutableMap<Location, BlockData>>

    fun runInternal(
        region: BoundingBox,
        blockLocation: Location,
        vec1: Vector,
        vec2: Vector,
        blockProcessor: (location: Location, blockChanges: MutableMap<Location, BlockData>) -> Unit
    ): Pair<Int, MutableMap<Location, BlockData>> {
        val blockVector = BlockVector3D(blockLocation, vec1, vec2)
        val world = blockLocation.world

        var num = 0
        val blocks = runXYZ(blockVector) { x, y, z, blockChanges ->
            val location = Utilities.toLocation(world, x, y, z)
            if (region.hasLocation(location.toVector())) {
                blockProcessor(location, blockChanges)
                num++
            }
        }

        return (num to blocks)
    }
}



internal inline fun runXYZ(
    blockVector3D: BlockVector3D,
    run: (Int, Int, Int, MutableMap<Location, BlockData>) -> Unit
): MutableMap<Location, BlockData> {
    val (minX, minY, minZ, maxX, maxY, maxZ) = blockVector3D
    val updateBlocks: MutableMap<Location, BlockData> = mutableMapOf()

    for (x in minX..maxX) {
        for (y in minY..maxY) {
            for (z in minZ..maxZ) {
                run(x, y, z, updateBlocks)
            }
        }
    }

    return updateBlocks
}
