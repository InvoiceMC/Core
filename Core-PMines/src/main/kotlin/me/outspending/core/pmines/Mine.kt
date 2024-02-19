package me.outspending.core.pmines

import org.bukkit.Location
import org.bukkit.block.data.BlockData
import org.bukkit.util.BoundingBox

interface Mine {

    companion object {
        fun default(): Mine? {
            // TODO: Set default locations
            return null
        }

        fun createMine(bottom: Location, top: Location): Mine {
            val boundingBox = BoundingBox.of(bottom, top)
            return MineImpl(bottom, top, boundingBox)
        }
    }

    fun getBlocks(): Map<Location, BlockData>
    fun getBottomLocation(): Location
    fun getTopLocation(): Location
    fun getRegion(): BoundingBox

    fun reset()
    fun expand(size: Int)
    fun increaseBlocks(amount: Int)
}