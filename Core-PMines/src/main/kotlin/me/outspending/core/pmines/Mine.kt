package me.outspending.core.pmines

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.block.data.BlockData
import org.bukkit.entity.Player
import org.bukkit.util.BoundingBox

interface Mine {

    companion object {
        fun default(): Mine {
            val world = Bukkit.getWorld("world")
            val topLocation = Location(world, 296.0, 29.0, -846.0)
            val bottomLocation = Location(world, 286.0, -15.0, -856.0)

            return createMine(bottomLocation, topLocation)
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

    fun removeBlock(location: Location)
    fun removeBlocks(locations: List<Location>)

    fun reset(player: Player): Int
    fun expand(size: Int)
    fun increaseBlocks(amount: Int)
}