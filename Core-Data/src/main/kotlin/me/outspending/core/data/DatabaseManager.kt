package me.outspending.core.data

import me.outspending.core.CoreHandler.core
import me.outspending.munch.connection.MunchConnection

private const val DATABASE_NAME = "database.db"

val database = MunchConnection.global()

object DatabaseManager {
    fun setupDatabase() {
        database.connect(core.dataFolder, DATABASE_NAME)
    }

    fun stopDatabase() {
        if (!database.isConnected()) return

        database.disconnect()
    }
}
