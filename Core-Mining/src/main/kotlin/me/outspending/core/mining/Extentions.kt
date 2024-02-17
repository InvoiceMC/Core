package me.outspending.core.mining

import org.bukkit.craftbukkit.v1_20_R3.entity.CraftPlayer
import org.bukkit.entity.Player

object Extentions {
    fun Player.getConnection() = (this as CraftPlayer).handle.connection
}