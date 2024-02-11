package me.outspending.core.storage

import me.outspending.core.Utilities.runTaskTimer
import me.outspending.core.core
import me.outspending.core.storage.data.CellData
import me.outspending.core.storage.data.PlayerData
import me.outspending.core.storage.registries.CellRegistry
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
    val database = MunchConnection.create()
    val munchPlayerData = Munch.create(PlayerData::class).process<UUID>()
    val munchCellData = Munch.create(CellData::class).process<String>()

    fun setupDatabase() {
        if (database.isConnected()) return

        database.connect(core.dataFolder, DATABASE_NAME)
        database.createTable(munchPlayerData)
        database.createTable(munchCellData)

        // Have no clue why this doesn't work in munch but :shrug:
        Reflections(SERIALIZER_PACKAGE)
            .getSubTypesOf(Serializer::class.java)
            .forEach {
                val serializer = it.getDeclaredConstructor().newInstance() as Serializer<*>
                SerializerFactory.registerSerializer(serializer)
            }

        runTaskTimer(6000, 6000) { PlayerRegistry.updateAllPlayerData() }
        runTaskTimer(6000, 6000) { CellRegistry.updateAllCells() }
    }

    fun stopDatabase() {
        if (!database.isConnected()) return

        val playerData = PlayerRegistry.playerData.values.toList()
        database.updateAllData(munchPlayerData, playerData)

        val cellData = CellRegistry.cells.values.toList()
        database.updateAllData(munchCellData, cellData)

        database.disconnect()
    }
    
    fun updateAllData() {
        runTaskTimer(6000, 6000) {
            PlayerRegistry.updateAllPlayerData()
            CellRegistry.updateAllCells()
        }
    }
}
