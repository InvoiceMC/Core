package me.outspending.core.pmines

import me.outspending.core.misc.WeightedCollection
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.data.BlockData
import org.bukkit.entity.Player
import org.bukkit.util.BoundingBox

interface Mine {

    companion object {
        private val DEFAULT_COLLECTION = WeightedCollection<BlockData>()
            .add(0.75, Material.STONE.createBlockData())
            .add(0.25, Material.COBBLESTONE.createBlockData())

        // TODO: Improve this slightly
        fun default(): Mine {
            val world = Bukkit.getWorld("world")
            val topLocation = Location(world, 296.0, 29.0, -846.0)
            val bottomLocation = Location(world, 286.0, -15.0, -856.0)

            return createMine(bottomLocation, topLocation)
        }

        fun createMine(bottom: Location, top: Location, blockWeights: WeightedCollection<BlockData> = DEFAULT_COLLECTION): Mine {
            val boundingBox = BoundingBox.of(bottom, top)
            val mine = MineImpl(bottom, top, boundingBox)
            mine.blockWeights = blockWeights

            return mine
        }
    }

    fun getBlockWeights(): WeightedCollection<BlockData>
    fun getBlocks(): Map<Location, BlockData>
    fun getBottomLocation(): Location
    fun getTopLocation(): Location
    fun getRegion(): BoundingBox

    fun getVolume(): Int
    fun getBlockCount(): Int

    fun removeBlock(location: Location)
    fun removeBlocks(locations: Set<Location>)

    fun forceReset(player: Player, mine: PrivateMine): Int
    fun reset(player: Player, mine: PrivateMine): Int
    fun canReset(): Boolean
    fun getResetTimeLeft(): Long
    fun getResetCooldown(): Long

    fun expand(size: Int = 1)
    fun increaseBlocks(amount: Int)
}