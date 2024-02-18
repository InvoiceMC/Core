package me.outspending.core.data

import me.outspending.core.Utilities.toComponent
import me.outspending.core.data.player.PlayerData
import me.outspending.core.helpers.enums.CustomSound
import net.kyori.adventure.key.Key
import net.kyori.adventure.sound.Sound
import org.bukkit.Bukkit
import java.util.*
import kotlin.time.measureTime

object DataHandler {
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

    /** This is for when the player joins the server */
    fun addData(uuid: UUID) {
        database.getData(munchPlayerData, uuid).thenAccept {
            val playerData: PlayerData
            if (it == null) {
                playerData = PlayerData(uuid)

                database.addData(munchPlayerData, playerData)
            } else {
                playerData = database.getData(munchPlayerData, uuid).get()!!
            }

            // Add the player's data to memory
            PlayerRegistry.addPlayer(uuid, playerData)
        }
    }

    fun updateData(uuid: UUID) {
        val playerData = PlayerRegistry.getPlayerData(uuid) ?: return
        database.updateData(munchPlayerData, playerData, uuid)
    }

    fun updateDataThenDelete(uuid: UUID) {
        updateData(uuid)

        PlayerRegistry.removePlayer(uuid)
    }

    fun updateAllData() {
        val data = PlayerRegistry.playerData.values.toList()
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