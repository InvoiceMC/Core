package me.outspending.core.mining

import me.outspending.core.misc.WeightedCollection
import me.outspending.core.pmines.Mine
import me.outspending.core.pmines.PrivateMine
import me.outspending.core.toLocation
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.block.data.BlockData
import org.bukkit.util.Vector

abstract class PacketShape {
    val NULLBLOCK: BlockData = Material.AIR.createBlockData()
    val MINE_WORLD: World by lazy { Bukkit.getWorld("testing")!! }

    abstract fun process(mine: PrivateMine, blockLocation: Location?, blockData: BlockData = NULLBLOCK): Int

    abstract fun process(mine: PrivateMine, blockLocation: Location?, blockData: BlockData = NULLBLOCK, perBlock: (Location, BlockData) -> Unit): Int

    abstract fun process(mine: PrivateMine, blockLocation: Location?, weightedBlockData: WeightedCollection<BlockData>): Int

    abstract fun process(mine: PrivateMine, blockLocation: Location?, weightedBlockData: WeightedCollection<BlockData>, perBlock: (Location, BlockData) -> Unit): Int

    fun runBetween(
        world: World,
        originLocation: Location,
        minVector: Vector,
        maxVector: Vector,
        blockProcessor: (Location) -> BlockData?
    ): Pair<Int, Map<Location, BlockData>> {
        val minLoc = originLocation.clone().add(minVector)
        val maxLoc = originLocation.clone().add(maxVector)

        return runBetween(world, minLoc, maxLoc, blockProcessor)
    }

    fun runBetween(
        world: World,
        minLocation: Location,
        maxLocation: Location,
        blockProcessor: (Location) -> BlockData?
    ): Pair<Int, Map<Location, BlockData>> {
        val minX = minLocation.blockX
        val minY = minLocation.blockY
        val minZ = minLocation.blockZ

        val maxX = maxLocation.blockX
        val maxY = maxLocation.blockY
        val maxZ = maxLocation.blockZ

        val blockDataMap = mutableMapOf<Location, BlockData>()
        for (x in minX..maxX) {
            for (y in minY..maxY) {
                for (z in minZ..maxZ) {
                    val location = toLocation(world, x, y, z)
                    val blockData = blockProcessor(location)

                    if (blockData != null) {
                        blockDataMap[location] = blockData
                    }
                }
            }
        }

        return (blockDataMap.size to blockDataMap)
    }

    fun updateBlocks(mine: Mine, blocksToRemove: List<Location>) = mine.removeBlocks(blocksToRemove)
}
