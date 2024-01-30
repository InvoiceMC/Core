package me.outspending.core.storage.database.impl

import me.outspending.core.storage.DataHandler
import me.outspending.core.storage.data.PlayerData
import me.outspending.core.storage.database.Database
import me.outspending.core.storage.database.DatabaseHandler
import me.outspending.core.storage.database.serializer.SerializerManager
import java.sql.SQLException
import java.util.*

class PlayerDatabase : Database<UUID, PlayerData> {
    private var sqlTable: String =
        """
        CREATE TABLE IF NOT EXISTS player_data (
            uuid TEXT PRIMARY KEY NOT NULL,
            balance REAL NOT NULL,
            gold INTEGER NOT NULL,
            blocks_broken INTEGER NOT NULL,
            prestige INTEGER NOT NULL,
            multiplier REAL NOT NULL,
            pmine_name TEXT NOT NULL,
            tag TEXT NOT NULL,
            cell_id TEXT
        );
        """
            .trimIndent()

    private var sqlUpdate: String =
        "UPDATE player_data SET balance = ?, gold = ?, blocks_broken = ?, prestige = ?, multiplier = ?, pmine_name = ?, tag = ?, cell_id = ? WHERE uuid = ?;"
    private var sqlInsert: String =
        "INSERT INTO player_data (uuid, balance, gold, blocks_broken, prestige, multiplier, pmine_name, tag, cell_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);"
    private var sqlDelete: String = "DELETE FROM player_data WHERE uuid = ?;"
    private var sqlSelect: String = "SELECT * FROM player_data WHERE uuid = ?;"
    private var sqlSelectAll: String = "SELECT * FROM player_data;"

    override fun createTable() {
        if (!DatabaseHandler.isConnected()) return

        val connection = DatabaseHandler.databaseConnection
        try {
            val statement = connection.prepareStatement(sqlTable)

            statement.execute()
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }

    override fun updateAllData() {
        if (!DatabaseHandler.isConnected()) return

        val connection = DatabaseHandler.databaseConnection
        try {
            val statement = connection.prepareStatement(sqlUpdate)

            DataHandler.playerData.forEach { (uuid, playerData) ->
                SerializerManager(playerData).serialize(statement) { index, _ ->
                    statement.setString(index, uuid.toString())
                }

                statement.addBatch()
            }

            statement.executeBatch()
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }

    override fun hasData(key: UUID): Boolean {
        if (!DatabaseHandler.isConnected()) return false

        val connection = DatabaseHandler.databaseConnection
        try {
            val statement = connection.prepareStatement(sqlSelect)

            statement.setString(1, key.toString())
            val resultSet = statement.executeQuery()

            return resultSet.next()
        } catch (e: SQLException) {
            e.printStackTrace()
        }

        return false
    }

    override fun getData(key: UUID): PlayerData? {
        if (!DatabaseHandler.isConnected()) return null

        val connection = DatabaseHandler.databaseConnection
        try {
            val statement = connection.prepareStatement(sqlSelect)
            statement.setString(1, key.toString())

            return SerializerManager.deserializeWithoutData(PlayerData::class, statement)
        } catch (e: SQLException) {
            e.printStackTrace()
        }

        return null
    }

    override fun getAllData(): List<PlayerData>? {
        if (!DatabaseHandler.isConnected()) return null

        val connection = DatabaseHandler.databaseConnection
        try {
            val statement = connection.prepareStatement(sqlSelectAll)
            val resultSet = statement.executeQuery()

            val list: MutableList<PlayerData> = mutableListOf()
            while (resultSet.next()) {
                val balance = resultSet.getDouble("balance")
                val gold = resultSet.getInt("gold")
                val blocksBroken = resultSet.getLong("blocks_broken")
                val prestige = resultSet.getInt("prestige")
                val multiplier = resultSet.getFloat("multiplier")
                val pmineName = resultSet.getString("pmine_name")
                val tag = resultSet.getString("tag")
                val cellId = resultSet.getString("cell_id")

                list.add(
                    PlayerData(balance, gold, blocksBroken, prestige, multiplier, pmineName, tag, cellId)
                )
            }

            return list
        } catch (e: SQLException) {
            e.printStackTrace()
        }

        return null
    }

    override fun deleteData(key: UUID) {
        if (!DatabaseHandler.isConnected()) return

        val connection = DatabaseHandler.databaseConnection
        try {
            val statement = connection.prepareStatement(sqlDelete)

            statement.setString(1, key.toString())

            statement.execute()
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }

    override fun updateData(
        key: UUID,
        value: PlayerData,
    ) {
        if (!DatabaseHandler.isConnected()) return

        val connection = DatabaseHandler.databaseConnection
        try {
            val statement = connection.prepareStatement(sqlUpdate)
            SerializerManager(value).serialize(statement) { index, _ ->
                statement.setString(index, key.toString())
            }

            statement.execute()
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }

    override fun createData(
        key: UUID,
        value: PlayerData,
    ) {
        if (!DatabaseHandler.isConnected()) return

        val connection = DatabaseHandler.databaseConnection
        try {
            val statement = connection.prepareStatement(sqlInsert)

            statement.setString(1, key.toString())
            statement.setDouble(2, value.balance)
            statement.setInt(3, value.gold)
            statement.setLong(4, value.blocksBroken)
            statement.setInt(5, value.prestige)
            statement.setFloat(6, value.multiplier)
            statement.setString(7, value.pmineName)
            statement.setString(8, value.tag)
            statement.setString(9, value.cellId)

            statement.execute()
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }
}
