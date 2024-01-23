package me.outspending.core.storage.database

import me.outspending.core.Core
import me.outspending.core.instance
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

class DatabaseHandler {
    companion object {
        lateinit var databaseConnection: Connection

        fun isConnected(): Boolean {
            return this::databaseConnection.isInitialized
        }

        fun setupDatabase() {
            val plugin: JavaPlugin = instance

            val dataFolder: File = plugin.dataFolder
            if (!dataFolder.exists()) {
                dataFolder.mkdir()
            }

            val databaseFile = File(dataFolder, "database.db")
            if (!databaseFile.exists()) {
                databaseFile.createNewFile()
            }

            try {
                databaseConnection =
                    DriverManager.getConnection("jdbc:sqlite:${databaseFile.absolutePath}")
            } catch (e: SQLException) {
                e.printStackTrace()
            }
        }

        fun closeConnection() {
            if (!this::databaseConnection.isInitialized) return

            try {
                databaseConnection.close()
            } catch (e: SQLException) {
                e.printStackTrace()
            }
        }
    }
}
