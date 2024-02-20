package me.outspending.core

import org.bukkit.Location
import org.bukkit.World
import org.bukkit.util.Vector
import kotlin.math.max
import kotlin.math.min

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

    constructor(
        bottomLocation: Location,
        topLocation: Location
    ) : this(
        min(bottomLocation.blockX, topLocation.blockX),
        min(bottomLocation.blockY, topLocation.blockY),
        min(bottomLocation.blockZ, topLocation.blockZ),
        max(bottomLocation.blockX, topLocation.blockX),
        max(bottomLocation.blockY, topLocation.blockY),
        max(bottomLocation.blockZ, topLocation.blockZ)
    )

    fun getMin(world: World) = Utilities.toLocation(world, minX, minY, minZ)
    fun getMax(world: World) = Utilities.toLocation(world, maxX, maxY, maxZ)
}