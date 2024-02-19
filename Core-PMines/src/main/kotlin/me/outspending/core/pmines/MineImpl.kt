package me.outspending.core.pmines

import org.bukkit.Location
import org.bukkit.block.data.BlockData
import org.bukkit.util.BoundingBox

class MineImpl internal constructor(val bottom: Location, val top: Location, val region: BoundingBox) : Mine {
    private val blocks: Map<Location, BlockData> = mutableMapOf()

    override fun getBlocks(): Map<Location, BlockData> = blocks
    override fun getBottomLocation(): Location = bottom
    override fun getTopLocation(): Location = top
    override fun getRegion(): BoundingBox = region

    override fun reset() {
        TODO("Not yet implemented")
    }

    override fun expand(size: Int) {
        TODO("Not yet implemented")
    }

    override fun increaseBlocks(amount: Int) {
        TODO("Not yet implemented")
    }
}