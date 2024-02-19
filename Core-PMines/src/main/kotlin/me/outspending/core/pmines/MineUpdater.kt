package me.outspending.core.pmines

import org.bukkit.Location
import org.bukkit.block.data.BlockData

object MineUpdater {

    fun resetMine(mine: Mine): Pair<Int, MutableMap<Location, BlockData>> {
        return (0 to mutableMapOf())
    }
}