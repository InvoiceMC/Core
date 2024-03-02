package me.outspending.core.data

import me.outspending.core.CoreHandler.core
import me.outspending.core.data.player.PlayerData
import me.outspending.core.data.player.playerDataManager
import me.outspending.core.runTaskTimer
import me.outspending.munch.Munch
import me.outspending.munch.connection.MunchConnection
import java.util.*

private const val SERIALIZER_PACKAGE = "me.outspending.core.data.serializers"
private const val DATABASE_NAME = "database.db"
private const val DATABASE_UPDATE_INTERVAL = 6000L

val munchPlayerData = Munch.create(PlayerData::class).process<UUID>()
val database = MunchConnection.global()

object DatabaseManager {
    fun setupDatabase() {
        database.connect(core.dataFolder, DATABASE_NAME)
        database.createTable(munchPlayerData)

        runTaskTimer(DATABASE_UPDATE_INTERVAL, DATABASE_UPDATE_INTERVAL, true) {
            DataSaver.updateAllData()
        }
    }

    fun stopDatabase() {
        if (!database.isConnected()) return

        val playerData = playerDataManager.getPlayerDataList()
        database.updateAllData(munchPlayerData, playerData)

        database.disconnect()
    }
}
