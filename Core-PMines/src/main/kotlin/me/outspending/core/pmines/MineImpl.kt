package me.outspending.core.pmines

import me.outspending.core.mining.Shape
import org.bukkit.Location
import org.bukkit.block.data.BlockData
import org.bukkit.entity.Player
import org.bukkit.util.BoundingBox

class MineImpl internal constructor(
    private val bottom: Location,
    private val top: Location,
    private val region: BoundingBox
) : Mine {
    private var blocks: MutableMap<Location, BlockData> = mutableMapOf()

    override fun getBlocks(): Map<Location, BlockData> = blocks
    override fun getBottomLocation(): Location = bottom
    override fun getTopLocation(): Location = top
    override fun getRegion(): BoundingBox = region

    override fun removeBlock(location: Location) {
        blocks.remove(location)
    }

    override fun removeBlocks(locations: List<Location>) {
        locations.forEach { blocks.remove(it) }
    }

    override fun reset(player: Player): Int {
        val (num, newBlocks) = MineUpdater.resetMine(player, this)
        blocks = newBlocks

        return num
    }

    override fun expand(size: Int) {
        TODO("Not yet implemented")
    }

    override fun increaseBlocks(amount: Int) {
        TODO("Not yet implemented")
    }
}