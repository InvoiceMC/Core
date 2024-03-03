package me.outspending.core.data.player

import com.github.shynixn.mccoroutine.bukkit.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import me.outspending.core.CoreHandler.core
import me.outspending.core.data.DataManager
import me.outspending.core.data.DataPersistenceHandler
import me.outspending.core.data.database
import me.outspending.core.helpers.FormatHelper.Companion.parse
import me.outspending.core.helpers.enums.CustomSound
import me.outspending.munch.Munch
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.*
import kotlin.time.measureTime

val munchPlayerData = Munch.create(PlayerData::class).process<UUID>()
val playerDataManager = PlayerDataManager()

class PlayerDataManager : DataManager<Player, PlayerData>() {
    private val saveSound: CustomSound.DataSave = CustomSound.DataSave(pitch = 0.65f)
    private val BROADCAST_MESSAGE: String =
        listOf(
                "",
                "<#7ee37b><b>ᴘʟᴀʏᴇʀᴅᴀᴛᴀ</b>",
                "  <gray>Successfully saved <#7ee37b><u>%s</u><gray> player(s)",
                "  <gray>data in <#7ee37b><u>%s</u><gray>!",
                ""
            )
            .joinToString("\n")

    private val persistenceHandler: DataPersistenceHandler<PlayerData, UUID> =
        PlayerDataPersistenceHandler()

    init {
        core.launch {
            while (true) {
                saveAllData()

                delay(300000)
            }
        }
    }

    override fun load() {
        database.createTable(munchPlayerData)
    }

    override fun unload() {
        saveAllData()
    }

    override fun saveAllData() {
        core.launch {
            val allData = data.values.toList()

            val time = measureTime {
                async(Dispatchers.IO) { database.updateAllData(munchPlayerData, allData) }.await()

                for (player in data.keys) {
                    saveSound.playSound(player)
                }
            }

            Bukkit.broadcast(BROADCAST_MESSAGE.format(data.size, time).parse())
        }
    }

    override fun loadData(key: Player) {
        if (data.containsKey(key)) return

        core.launch {
            data[key] = async(Dispatchers.IO) { persistenceHandler.load(key.uniqueId) }.await()
        }
    }

    override fun unloadData(key: Player) {
        if (!data.containsKey(key)) return

        val playerData: PlayerData? = data.remove(key)
        playerData?.let {
            core.launch {
                async(Dispatchers.IO) { persistenceHandler.save(key.uniqueId, it) }.await()
            }
        }
    }

    override fun saveData(key: Player) {
        if (!data.containsKey(key)) return

        val playerData: PlayerData = getData(key)
        core.launch {
            async(Dispatchers.IO) { persistenceHandler.save(key.uniqueId, playerData) }.await()
        }
    }

    override fun getData(key: Player): PlayerData = data[key]!!
}
