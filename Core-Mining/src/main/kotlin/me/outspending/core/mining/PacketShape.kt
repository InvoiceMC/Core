package me.outspending.core.mining

import me.outspending.core.misc.WeightedCollection
import me.outspending.core.pmines.Mine
import me.outspending.core.pmines.PrivateMine
import org.bukkit.Location
import org.bukkit.block.data.BlockData
import org.bukkit.util.Vector

interface PacketShape {

    fun process(mine: PrivateMine, blockLocation: Location?, blockData: BlockData): Int

    fun process(mine: PrivateMine, blockLocation: Location?, weightedBlockData: WeightedCollection<BlockData>): Int

    private fun runBetween(minVec: Vector, maxVector: Vector, blockProcessor: (Location, MutableMap<Location, BlockData>) -> Int) {
        val minX = minVec.blockX
        val minY = minVec.blockY
        val minZ = minVec.blockZ

        val maxX = maxVector.blockX
        val maxY = maxVector.blockY
        val maxZ = maxVector.blockZ

        // TODO: Was working on this!!!
        val
        for (x in minX..maxX) {
            for (y in minY..maxY) {
                for (z in minZ..maxZ) {
                    val location = Location(null, x.toDouble(), y.toDouble(), z.toDouble())
                    blockProcessor(location, mutableMapOf())
                }
            }
        }
    }

    private fun updateBlocks(mine: Mine, blocksToRemove: List<Location>) = mine.removeBlocks(blocksToRemove)
}
