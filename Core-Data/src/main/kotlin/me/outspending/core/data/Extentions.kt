package me.outspending.core.data

import me.outspending.core.data.player.PlayerData
import org.bukkit.entity.Player

object Extentions {
    fun Player.getData(): PlayerData? = PlayerRegistry.getPlayerData(this.uniqueId)
    fun Player.updateData() = DataHandler.updateData(this.uniqueId)
}