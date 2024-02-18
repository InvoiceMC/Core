package me.outspending.core.data.player

import me.outspending.core.data.DataPersistenceHandler
import me.outspending.core.data.database
import me.outspending.core.data.munchPlayerData
import java.util.*

class PlayerDataPersistenceHandler : DataPersistenceHandler<PlayerData, UUID> {

    override fun save(value: UUID, data: PlayerData?) {
        require(data != null) { "Data cannot be null" }

        database.updateData(munchPlayerData, data, value)
    }

    override fun load(value: UUID): PlayerData {
        val playerData: PlayerData? = database.getData(munchPlayerData, value).join()
        val loadedData = playerData ?: PlayerData(value)

        return loadedData
    }
}