package me.outspending.core.data

import me.outspending.core.data.player.PlayerData
import me.outspending.core.data.player.playerDataManager
import org.bukkit.entity.Player

fun Player.getData(): PlayerData = playerDataManager.getData(this)
fun Player.savePlayerData() = playerDataManager.saveData(this)