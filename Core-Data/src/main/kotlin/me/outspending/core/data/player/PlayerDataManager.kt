package me.outspending.core.data.player

import com.github.shynixn.mccoroutine.bukkit.launch
import kotlinx.coroutines.*
import me.outspending.core.CoreHandler.core
import me.outspending.core.data.DataPersistenceHandler
import me.outspending.core.data.database
import me.outspending.core.data.munchPlayerData
import me.outspending.core.helpers.FormatHelper.Companion.parse
import me.outspending.core.helpers.enums.CustomSound
import me.outspending.core.runTaskLater
import me.outspending.core.runTaskTimer
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.*
import kotlin.time.measureTime

val playerDataManager = PlayerDataManager()

class PlayerDataManager {
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

    private val playerData: HashMap<Player, PlayerData> = HashMap()
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

    fun clear() {
        playerData.clear()
    }

    suspend fun saveAllData() {
        core.launch {
            val allData = playerData.values.toList()

            val time = measureTime {
                val deferred = async(Dispatchers.IO) {
                    database.updateAllData(munchPlayerData, allData)
                    Bukkit.broadcast("Running in thread: <white>${Thread.currentThread().name}".parse(true))
                }
                deferred.await()

                for (player in playerData.keys) {
                    saveSound.playSound(player)
                }
            }

            Bukkit.broadcast(BROADCAST_MESSAGE.format(playerData.size, time).parse())
        }
    }

    fun loadPlayerData(player: Player) {
        if (playerData.containsKey(player)) return

        playerData[player] = persistenceHandler.load(player.uniqueId)

        Bukkit.broadcast("Running in thread: <white>${Thread.currentThread().name}".parse(true))
    }

    fun unloadPlayerData(player: Player) {
        if (!playerData.containsKey(player)) return

        val playerData: PlayerData? = playerData.remove(player)
        playerData?.let {
            persistenceHandler.save(player.uniqueId, it)
        }
        Bukkit.broadcast("Running in thread: <white>${Thread.currentThread().name}".parse(true))
    }

    fun savePlayerData(player: Player) {
        if (!playerData.containsKey(player)) return

        val playerData: PlayerData = getPlayerData(player)
        persistenceHandler.save(player.uniqueId, playerData)
        Bukkit.broadcast("Running in thread: <white>${Thread.currentThread().name}".parse(true))
    }

    fun getPlayerData(player: Player): PlayerData = playerData[player]!!

    fun getPlayerDataList(): List<PlayerData> = playerData.values.toList()

    fun getAllPlayerData(): HashMap<Player, PlayerData> = playerData
}
