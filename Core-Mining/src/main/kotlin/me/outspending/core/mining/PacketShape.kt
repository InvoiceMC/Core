package me.outspending.core.mining

import me.outspending.core.Utilities
import me.outspending.core.misc.WeightedCollection
import me.outspending.core.pmines.Mine
import me.outspending.core.pmines.PrivateMine
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

    abstract fun process(mine: PrivateMine, blockLocation: Location?, weightedBlockData: WeightedCollection<BlockData>): Int

    fun runBetween(
        world: World,
        minVec: Vector,
        maxVector: Vector,
        blockProcessor: (Location) -> BlockData?
    ): Pair<Int, Map<Location, BlockData>> {
        val minX = minVec.blockX
        val minY = minVec.blockY
        val minZ = minVec.blockZ

        val maxX = maxVector.blockX
        val maxY = maxVector.blockY
        val maxZ = maxVector.blockZ

        // TODO: DEBUG
        println("minX: $minX, minY: $minY, minZ: $minZ")
        println("maxX: $maxX, maxY: $maxY, maxZ: $maxZ")
        val blockDataMap = mutableMapOf<Location, BlockData>()
        for (x in minX..maxX) {
            for (y in minY..maxY) {
                for (z in minZ..maxZ) {
                    val location = Utilities.toLocation(world, x, y, z)
                    val blockData = blockProcessor(location)

                    // TODO: DEBUG
                    if (blockData != null) {
                        println(location)
                        blockDataMap[location] = blockData
                    }
                }
            }
        }

        return (blockDataMap.size to blockDataMap)
    }

    fun updateBlocks(mine: Mine, blocksToRemove: List<Location>) = mine.removeBlocks(blocksToRemove)
}
