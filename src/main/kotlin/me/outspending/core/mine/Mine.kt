package me.outspending.core.mine

import org.bukkit.Location

// Loc1 = Top left corner
// Loc2 = Bottom right corner
class Mine(var loc1: Location, var loc2: Location, var blocks: List<MineBlock>) {
    fun reset() {

    }

    fun expandMine() {

    }

    fun getMineSize(): Int = ((loc1.x - loc2.x) * (loc1.z - loc2.z)).toInt()
}
