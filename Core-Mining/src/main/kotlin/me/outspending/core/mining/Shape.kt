package me.outspending.core.mining

import me.outspending.core.Utilities
import me.outspending.core.misc.WeightedCollection
import org.bukkit.Location
import org.bukkit.block.data.BlockData
import org.bukkit.util.Vector
import kotlin.math.max
import kotlin.math.min

interface Shape {

    fun run(
        blockLocation: Location,
        blockData: BlockData
    ): Pair<Int, MutableMap<Location, BlockData>>

    fun run(
        blockLocation: Location,
        weightedCollection: WeightedCollection<BlockData>
    ): Pair<Int, MutableMap<Location, BlockData>>

    fun runInternal(
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
            blockProcessor(location, blockChanges)

            num++
        }

        return (num to blocks)
    }

    data class BlockVector3D(
        val minX: Int,
        val minY: Int,
        val minZ: Int,
        val maxX: Int,
        val maxY: Int,
        val maxZ: Int
    ) {
        constructor(
            blockLocation: Location,
            vec1: Vector,
            vec2: Vector
        ) : this(
            min(blockLocation.blockX - vec1.blockX, blockLocation.blockX - vec2.blockX),
            min(blockLocation.blockY - vec1.blockY, blockLocation.blockY - vec2.blockY),
            min(blockLocation.blockZ - vec1.blockZ, blockLocation.blockZ - vec2.blockZ),
            max(blockLocation.blockX - vec1.blockX, blockLocation.blockX - vec2.blockX),
            max(blockLocation.blockY - vec1.blockY, blockLocation.blockY - vec2.blockY),
            max(blockLocation.blockZ - vec1.blockZ, blockLocation.blockZ - vec2.blockZ)
        )
    }
}



internal inline fun runXYZ(
    blockVector3D: Shape.BlockVector3D,
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
