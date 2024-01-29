package me.outspending.core.utils

import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.data.BlockData
import org.bukkit.util.Vector
import kotlin.math.max
import kotlin.math.min

fun interface Shape {

    fun run(blockLocation: Location, blockData: BlockData): Pair<Int, MutableMap<Location, BlockData>>

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

internal inline fun runXZ(
    blockVector3D: Shape.BlockVector3D,
    run: (Int, Int, Int, MutableMap<Location, BlockData>) -> Unit
): MutableMap<Location, BlockData> {
    val (minX, minY, minZ, maxX, _, maxZ) = blockVector3D
    val updateBlocks: MutableMap<Location, BlockData> = mutableMapOf()

    for (x in minX..maxX) {
        for (z in minZ..maxZ) {
            run(x, minY, z, updateBlocks)
        }
    }

    return updateBlocks
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