package me.outspending.core.pmines

import me.outspending.core.Utilities
import me.outspending.core.mining.Shape
import me.outspending.core.mining.sync.PacketSync
import me.outspending.core.misc.WeightedCollection
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.data.BlockData
import org.bukkit.entity.Player

object MineUpdater {
    private val DEFAULT = WeightedCollection<BlockData>()
        .add(0.75, Material.STONE.createBlockData())
        .add(0.25, Material.COBBLESTONE.createBlockData())

    fun resetMine(player: Player, mine: Mine): Pair<Int, MutableMap<Location, BlockData>> {
        val bottom = mine.getBottomLocation()
        val top = mine.getTopLocation()
        val world = player.world

        val vector = Shape.BlockVector3D(bottom, top)
        val min = vector.getMin(world)
        val max = vector.getMax(world)

        val blocks: MutableMap<Location, BlockData> = mutableMapOf()
        for (x in min.blockX..max.blockX) {
            for (y in min.blockY..max.blockY) {
                for (z in min.blockZ..max.blockZ) {
                    val location = Utilities.toLocation(world, x, y, z)
                    blocks[location] = DEFAULT.next()
                }
            }
        }

        PacketSync.syncBlocks(min, blocks)
        return (blocks.size to blocks)
    }
}