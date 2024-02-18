package me.outspending.core.data

import me.outspending.core.Utilities.runAsync
import me.outspending.core.Utilities.toComponent
import me.outspending.core.data.player.PlayerData
import net.kyori.adventure.key.Key
import net.kyori.adventure.sound.Sound
import org.bukkit.Bukkit
import java.util.*
import kotlin.time.measureTime

object PlayerRegistry {
    val playerData: MutableMap<UUID, PlayerData> = mutableMapOf()

    fun addPlayer(uuid: UUID, playerData: PlayerData) {
        PlayerRegistry.playerData[uuid] = playerData
    }

    fun removePlayer(uuid: UUID) = playerData.remove(uuid)

    fun getPlayerData(uuid: UUID): PlayerData? = playerData[uuid]

    fun updatePlayerData(uuid: UUID, playerData: PlayerData) {
        database.updateData(munchPlayerData, playerData, uuid)
    }
}
