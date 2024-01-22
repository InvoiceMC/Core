package me.outspending.core.storage

import me.outspending.core.Core
import me.outspending.core.utils.Utilities.runAsync
import me.outspending.core.utils.Utilities.runTaskTimerAsynchronously
import me.outspending.core.utils.Utilities.toComponent
import net.kyori.adventure.key.Key
import net.kyori.adventure.sound.Sound
import org.bukkit.Bukkit
import java.util.*
import kotlin.time.measureTime

class DataHandler {
    companion object {
        private val BROADCAST_MESSAGE: String =
            listOf(
                    "",
                    "<#7ee37b><b>ᴘʟᴀʏᴇʀᴅᴀᴛᴀ",
                    "  <gray>Successfully saved <#7ee37b><u>%s</u><gray> player(s)",
                    "  <gray>data in <#7ee37b><u>%s</u><gray>!",
                    ""
                )
                .joinToString("\n")

        val playerData: MutableMap<UUID, PlayerData> = mutableMapOf()

        fun addPlayer(
            uuid: UUID,
            playerData: PlayerData,
        ) {
            this.playerData[uuid] = playerData
        }

        fun removePlayer(uuid: UUID) {
            this.playerData.remove(uuid)
        }

        fun getPlayerData(uuid: UUID): PlayerData? {
            return this.playerData[uuid]
        }

        fun updateAllPlayerData() {
            val database = Core.playerDatabase

            runAsync {
                val time = measureTime { database.updateAllData() }
                val players = Bukkit.getOnlinePlayers()

                players.forEach { player ->
                    player.playSound(
                        Sound.sound(Key.key("block.note_block.bit"), Sound.Source.PLAYER, .5f, 1.5f)
                    )
                }

                Bukkit.broadcast(BROADCAST_MESSAGE.format(players.size, time).toComponent())
            }
        }

        fun startup() = runTaskTimerAsynchronously(6000, 6000) { updateAllPlayerData() }
    }
}
