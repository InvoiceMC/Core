package me.outspending.core.data

import me.outspending.core.CoreHandler.core
import me.outspending.core.Utilities.runTaskTimer
import me.outspending.core.data.player.PlayerData
import me.outspending.munch.Munch
import me.outspending.munch.connection.MunchConnection
import me.outspending.munch.serializer.Serializer
import me.outspending.munch.serializer.SerializerFactory
import org.reflections.Reflections
import org.reflections.scanners.SubTypesScanner
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

        // Have no clue why this doesn't work in munch but :shrug:
        Reflections(SERIALIZER_PACKAGE)
            .getSubTypesOf(Serializer::class.java)
            .forEach {
                val serializer = it.getDeclaredConstructor().newInstance() as Serializer<*>
                SerializerFactory.registerSerializer(serializer)
            }

        runTaskTimer(DATABASE_UPDATE_INTERVAL, DATABASE_UPDATE_INTERVAL, true) {
            DataHandler.updateAllData()
        }
    }

    fun stopDatabase() {
        if (!database.isConnected()) return

        val playerData = PlayerRegistry.playerData.values.toList()
        database.updateAllData(munchPlayerData, playerData)

        database.disconnect()
    }
}
