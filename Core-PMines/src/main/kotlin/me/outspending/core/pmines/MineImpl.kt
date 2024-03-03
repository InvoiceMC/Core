package me.outspending.core.pmines

import me.outspending.core.misc.WeightedCollection
import org.bukkit.Location
import org.bukkit.block.data.BlockData
import org.bukkit.entity.Player
import org.bukkit.util.BoundingBox
import kotlin.math.abs

private const val RESET_COOLDOWN: Long = 60000 // in milliseconds = 1 Minute

class MineImpl internal constructor(
    private var bottom: Location,
    private var top: Location,
    private val region: BoundingBox
) : Mine {
    internal var blockWeights = WeightedCollection<BlockData>()
    private var blocks: MutableMap<Location, BlockData> = mutableMapOf()
    private var lastReset: Long = 0

    override fun getBlockWeights(): WeightedCollection<BlockData> = blockWeights
    override fun getBlocks(): Map<Location, BlockData> = blocks
    override fun getBottomLocation(): Location = bottom
    override fun getTopLocation(): Location = top
    override fun getRegion(): BoundingBox = region

    override fun getVolume(): Int {
        val min = bottom.toVector()
        val max = top.toVector()

        val difference = max.subtract(min)
        val volume = abs(difference.x) * abs(difference.y) * abs(difference.z)

        return volume.toInt()
    }

    override fun getBlockCount(): Int = blocks.size

    override fun removeBlock(location: Location) {
        blocks.remove(location)
    }

    override fun removeBlocks(locations: Set<Location>) {
        blocks.keys.removeAll(locations)
    }

    override fun forceReset(player: Player, mine: PrivateMine): Int {
        val (num, newBlocks) = MineUpdater.resetMine(player, mine, blockWeights)
        blocks = newBlocks
        lastReset = System.currentTimeMillis()

        return num
    }

    override fun reset(player: Player, mine: PrivateMine): Int? {
        return if (canReset()) forceReset(player, mine) else null
    }

    override fun canReset(): Boolean = getResetCooldown() >= RESET_COOLDOWN
    override fun getResetTimeLeft(): Long = RESET_COOLDOWN - getResetCooldown()
    override fun getResetCooldown(): Long = System.currentTimeMillis() - lastReset

    override fun expand(size: Int) {
        val doubleSize = size.toDouble()

        top = top.clone().add(doubleSize, 0.0, doubleSize)
        bottom = bottom.clone().add(-doubleSize, 0.0, -doubleSize)

        region.resize(top.x, top.y, top.z, bottom.x, bottom.y, bottom.z)
    }

    override fun increaseBlocks(amount: Int) {
        TODO("Not yet implemented")
    }
}