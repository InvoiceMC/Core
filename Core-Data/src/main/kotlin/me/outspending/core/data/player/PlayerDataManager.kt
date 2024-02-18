package me.outspending.core.data.player

import me.outspending.core.data.DataPersistenceHandler
import java.util.*

class PlayerDataManager {

    private val playerData: MutableMap<UUID, PlayerData> = mutableMapOf()
    private val persistenceHandler: DataPersistenceHandler<PlayerData, UUID> = PlayerDataPersistenceHandler()

    fun loadPlayerData(uuid: UUID) {
        val playerData: PlayerData = persistenceHandler.load(uuid)
        this.playerData[uuid] = playerData
    }

    fun unloadPlayerData(uuid: UUID) {
        val playerData: PlayerData? = playerData.remove(uuid)
        require(playerData != null) { "Player data for $uuid is null" }

        persistenceHandler.save(uuid, playerData)
    }

    fun savePlayerData(uuid: UUID) {
        val playerData: PlayerData = getPlayerData(uuid)
        persistenceHandler.save(uuid, playerData)
    }

    fun getPlayerData(uuid: UUID): PlayerData =
        playerData[uuid] ?: throw IllegalArgumentException("Player data for $uuid is null")

    fun getPlayerDataList(): List<PlayerData> = playerData.values.toList()

    fun getAllPlayerData(): Map<UUID, PlayerData> = playerData

}