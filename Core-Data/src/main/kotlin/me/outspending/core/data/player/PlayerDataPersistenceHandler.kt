package me.outspending.core.data.player

import me.outspending.core.data.DataPersistenceHandler
import me.outspending.core.data.database
import me.outspending.core.data.munchPlayerData
import java.util.*

class PlayerDataPersistenceHandler : DataPersistenceHandler<PlayerData, UUID> {

    override fun save(value: UUID, data: PlayerData?) {
        requireNotNull(data) { "Player data for $value is null" }

        database.hasData(munchPlayerData, value).thenAcceptAsync { hasData ->
            if (hasData!!) database.updateData(munchPlayerData, data, value)
            else database.addData(munchPlayerData, data)
        }
    }

    override fun load(value: UUID): PlayerData {
        val playerData: PlayerData? = database.getData(munchPlayerData, value).join()
        val loadedData = playerData ?: PlayerData(value)

        return loadedData
    }
}