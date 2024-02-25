package me.outspending.core.pmines

import me.outspending.core.misc.WeightedCollection
import org.bukkit.Location
import org.bukkit.block.data.BlockData
import org.bukkit.entity.Player
import org.bukkit.util.BoundingBox

private const val RESET_COOLDOWN: Long = 60000 // in milliseconds = 1 Minute

class MineImpl internal constructor(
    private var bottom: Location,
    private var top: Location,
    private val region: BoundingBox
) : Mine {
    internal var blockWeights = WeightedCollection<BlockData>()
    private var blocks: MutableMap<Location, BlockData> = mutableMapOf()
    private var lastReset: Long = 0

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

    override fun reset(player: Player, mine: PrivateMine): Int? {
        return if (canReset()) {
            val (num, newBlocks) = MineUpdater.resetMine(player, mine, blockWeights)
            blocks = newBlocks
            lastReset = System.currentTimeMillis()

            num
        } else {
            null
        }
    }

    override fun canReset(): Boolean = getResetCooldown() >= RESET_COOLDOWN
    override fun getResetTimeLeft(): Long = RESET_COOLDOWN - getResetCooldown()
    override fun getResetCooldown(): Long = System.currentTimeMillis() - lastReset

    override fun expand(size: Int) {
        TODO("Not yet implemented")
    }

    override fun increaseBlocks(amount: Int) {
        TODO("Not yet implemented")
    }
}