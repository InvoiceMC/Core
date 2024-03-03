package me.outspending.core.pmines

import me.outspending.core.BlockVector3D
import me.outspending.core.misc.WeightedCollection
import me.outspending.core.pmines.sync.PacketSync
import me.outspending.core.toLocation
import org.bukkit.Location
import org.bukkit.block.data.BlockData
import org.bukkit.entity.Player
import kotlin.math.abs

object MineUpdater {
    fun resetMine(player: Player, privateMine: PrivateMine, blockWeights: WeightedCollection<BlockData>): Pair<Int, MutableMap<Location, BlockData>> {
        val mine = privateMine.getMine()
        println(mine.getRegion())

        val bottom = mine.getBottomLocation()
        val top = mine.getTopLocation()
        val world = player.world

        val vector = BlockVector3D(bottom, top)
        val min = vector.getMin(world)
        val max = vector.getMax(world)

        val blocks: HashMap<Location, BlockData> = HashMap()
        for (x in min.blockX..max.blockX) {
            for (y in min.blockY..max.blockY) {
                for (z in min.blockZ..max.blockZ) {
                    val location = toLocation(world, x, y, z)
                    blocks[location] = blockWeights.next()
                }
            }
        }

        PacketSync.syncBlocks(privateMine, blocks)
        return (blocks.size to blocks)
    }
}