package me.outspending.core.data

import me.outspending.core.data.player.PlayerData
import me.outspending.core.data.player.playerDataManager
import org.bukkit.entity.Player

object Extensions {
    fun Player.getData(): PlayerData = playerDataManager.getPlayerData(this)
    fun Player.savePlayerData() = playerDataManager.savePlayerData(this)
}