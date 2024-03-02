package me.outspending.core.data

import me.outspending.core.CoreHandler.core
import me.outspending.core.data.player.PlayerData
import me.outspending.core.data.player.playerDataManager
import me.outspending.munch.Munch
import me.outspending.munch.connection.MunchConnection
import java.util.*

private const val DATABASE_NAME = "database.db"

val munchPlayerData = Munch.create(PlayerData::class).process<UUID>()
val database = MunchConnection.global()

object DatabaseManager {
    fun setupDatabase() {
        database.connect(core.dataFolder, DATABASE_NAME)
        database.createTable(munchPlayerData)
    }

    fun stopDatabase() {
        if (!database.isConnected()) return

        val playerData = playerDataManager.getPlayerDataList()
        database.updateAllData(munchPlayerData, playerData)

        database.disconnect()
    }
}
