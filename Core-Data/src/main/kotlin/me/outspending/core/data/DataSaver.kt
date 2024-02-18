package me.outspending.core.data

import me.outspending.core.Utilities.toComponent
import me.outspending.core.data.player.playerDataManager
import me.outspending.core.helpers.enums.CustomSound
import org.bukkit.Bukkit
import kotlin.time.measureTime

object DataSaver {
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

    fun updateAllData() {
        val data = playerDataManager.getAllPlayerData()
        val time = measureTime {
            database.updateAllData(munchPlayerData, data)
        }

        val players = Bukkit.getOnlinePlayers()
        players.forEach { player ->
            saveSound.playSound(player)
        }

        Bukkit.broadcast(BROADCAST_MESSAGE.format(players.size, time).toComponent())
    }
}