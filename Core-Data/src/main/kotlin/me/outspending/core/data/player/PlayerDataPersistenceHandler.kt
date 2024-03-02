package me.outspending.core.data.player

import me.outspending.core.data.DataPersistenceHandler
import me.outspending.core.data.database
import me.outspending.core.data.munchPlayerData
import java.util.*

class PlayerDataPersistenceHandler : DataPersistenceHandler<PlayerData, UUID> {

    override fun save(value: UUID, data: PlayerData?) {
        requireNotNull(data) { "Player data for $value is null" }

        val hasData = database.hasData(munchPlayerData, value) ?: false

        if (hasData) database.updateData(munchPlayerData, data, value)
        else database.addData(munchPlayerData, data)
    }

    override fun load(value: UUID): PlayerData {
        val playerData: PlayerData? = database.getData(munchPlayerData, value)
        val loadedData = playerData ?: PlayerData(value)

        return loadedData
    }
}