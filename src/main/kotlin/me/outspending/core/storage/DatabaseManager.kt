package me.outspending.core.storage

import me.outspending.core.Utilities.runTaskTimer
import me.outspending.core.core
import me.outspending.core.storage.data.PlayerData
import me.outspending.core.storage.registries.PlayerRegistry
import me.outspending.munch.Munch
import me.outspending.munch.connection.MunchConnection
import me.outspending.munch.serializer.Serializer
import me.outspending.munch.serializer.SerializerFactory
import org.reflections.Reflections
import java.util.*

const val SERIALIZER_PACKAGE = "me.outspending.core.storage.serializers"
const val DATABASE_NAME = "database.db"

object DatabaseManager {
    val munchPlayerData = Munch.create(PlayerData::class).process<UUID>()
    val database = MunchConnection.global()

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

        updateAllData()
    }

    fun stopDatabase() {
        if (!database.isConnected()) return

        val playerData = PlayerRegistry.playerData.values.toList()
        database.updateAllData(munchPlayerData, playerData)

        database.disconnect()
    }
    
    private fun updateAllData() {
        runTaskTimer(6000, 6000) {
            PlayerRegistry.updateAllPlayerData()
        }
    }
}
