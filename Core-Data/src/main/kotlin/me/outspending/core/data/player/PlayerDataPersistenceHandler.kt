package me.outspending.core.data.player

import me.outspending.core.data.DataPersistenceHandler
import me.outspending.core.data.database
import me.outspending.core.data.munchPlayerData
import java.util.*

class PlayerDataPersistenceHandler : DataPersistenceHandler<PlayerData, UUID> {

    override fun save(value: UUID, data: PlayerData?) {
        data?.let {
            database.hasData(munchPlayerData, value).thenAccept { hasData ->
                if (hasData!!) database.updateData(munchPlayerData, it, value)
                else database.addData(munchPlayerData, it)
            }
        }
    }

    override fun load(value: UUID): PlayerData {
        val playerData: PlayerData? = database.getData(munchPlayerData, value).join()
        val loadedData = playerData ?: PlayerData(value)

        return loadedData
    }
}