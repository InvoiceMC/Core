package me.outspending.core.pmines

import me.outspending.core.helpers.FormatHelper.Companion.parse
import me.outspending.core.mining.MineUtils
import me.outspending.core.misc.WeightedCollection
import org.bukkit.Location
import org.bukkit.block.data.BlockData
import org.bukkit.entity.Player

class Mine(private val minLocation: Location, private val maxLocation: Location, val blocks: WeightedCollection<BlockData>) {
    private val resetTime: Long = 0L

    fun getVolume(): Int {
        val x = maxLocation.x - minLocation.x
        val y = maxLocation.y - minLocation.y
        val z = maxLocation.z - minLocation.z

        return (x * y * z).toInt()
    }

    fun reset(receivingPlayer: Player) {
        val time = System.currentTimeMillis()
        if (time < resetTime) {
            val seconds = (resetTime - time) / 1000
            receivingPlayer.sendMessage("You cannot reset your mine for another <second>${"%.2f".format(seconds)} seconds<gray>!".parse(true))
            return
        }

        MineUtils.setBlocksBetween(
            receivingPlayer,
            minLocation,
            maxLocation,
            blocks,
            true
        )
    }

}