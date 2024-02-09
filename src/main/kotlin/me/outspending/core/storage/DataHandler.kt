package me.outspending.core.storage

import me.outspending.core.Utilities.runAsync
import me.outspending.core.Utilities.toComponent
import me.outspending.core.storage.DatabaseHandler.database
import me.outspending.core.storage.DatabaseHandler.munchPlayerData
import me.outspending.core.storage.data.PlayerData
import net.kyori.adventure.key.Key
import net.kyori.adventure.sound.Sound
import org.bukkit.Bukkit
import java.util.*
import kotlin.time.measureTime

object DataHandler {

    private val BROADCAST_MESSAGE: String =
        listOf(
                "",
                "<#7ee37b><b>ᴘʟᴀʏᴇʀᴅᴀᴛᴀ</b>",
                "  <gray>Successfully saved <#7ee37b><u>%s</u><gray> player(s)",
                "  <gray>data in <#7ee37b><u>%s</u><gray>!",
                ""
            )
            .joinToString("\n")

    val playerData: MutableMap<UUID, PlayerData> = mutableMapOf()

    fun addPlayer(uuid: UUID, playerData: PlayerData) {
        this.playerData[uuid] = playerData
    }

    fun removePlayer(uuid: UUID) = this.playerData.remove(uuid)

    fun getPlayerData(uuid: UUID): PlayerData? = this.playerData[uuid]

    fun updateAllPlayerData() {
        runAsync {
            val time = measureTime {
                database.updateAllData(munchPlayerData, playerData.values.toList())
            }
            val players = Bukkit.getOnlinePlayers()
            players.forEach { player ->
                player.playSound(
                    Sound.sound(Key.key("block.note_block.bit"), Sound.Source.PLAYER, .5f, 1.5f)
                )
            }

            Bukkit.broadcast(BROADCAST_MESSAGE.format(players.size, time).toComponent())
        }
    }
}
