package me.outspending.core.mine

import me.outspending.core.utils.FAWEUtils
import org.bukkit.Location

// Loc1 = Top left corner
// Loc2 = Bottom right corner
class Mine(var loc1: Location, var loc2: Location, var blocks: List<MineBlock>) {
    fun reset() = FAWEUtils.setBlocks(loc1, loc2, blocks.toTypedArray())

    fun expandMine() {
        loc1.add(-1.0, 0.0, -1.0)
        loc2.add(1.0, 0.0, 1.0)

        // TODO: There is 2 blocks below the mine that also have to be set to air, which currently doesn't happen
        FAWEUtils.setWalls(loc1, loc2)
        reset()
    }

    fun getMineSize(): Int = ((loc1.x - loc2.x) * (loc1.z - loc2.z)).toInt()
}
