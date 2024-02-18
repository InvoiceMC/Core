package me.outspending.core.listeners.types

import me.outspending.core.Utilities.runAsync
import me.outspending.core.data.DataHandler
import me.outspending.core.data.PlayerRegistry
import me.outspending.core.helpers.FormatHelper.Companion.parse
import me.outspending.core.helpers.enums.CustomSound
import me.outspending.core.mining.duplex.PacketListeners
import me.outspending.core.quests.QuestsHandler
import me.outspending.core.scoreboard.scoreboardManager
import net.kyori.adventure.text.Component
import net.kyori.adventure.title.Title
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerLevelChangeEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import java.util.*
import kotlin.time.measureTime

class PlayerListeners : Listener {
    @EventHandler
    fun onPlayerJoin(e: PlayerJoinEvent) {
        val player: Player = e.player
        val uuid: UUID = player.uniqueId

        // Join message
        val joinMessage: Component =
            if (!player.hasPlayedBefore()) {
                "<main>${player.name} <white>has joined for the first time! <gray>[<second>#${Bukkit.getOfflinePlayers().size}<gray>]"
                    .parse()
            } else {
                "<gray>${player.name} has joined!".parse()
            }
        e.joinMessage(joinMessage)

        // Add Night Vision
        player.addPotionEffect(
            PotionEffect(PotionEffectType.NIGHT_VISION, Int.MAX_VALUE, 1, false, false)
        )

        // Load data
        runAsync {
            val time = measureTime {
                DataHandler.addData(uuid)
            }

            // Show data loaded title
            player.showTitle(
                Title.title(
                    "<main><b>ᴅᴀᴛᴀʙᴀꜱᴇ</b>".parse(),
                    "<gray><i>Loaded your data in <second><u>$time</u><gray>!".parse(),
                ),
            )

            // Play sound
            CustomSound.Success(pitch = 1.65F).playSound(player)

            // Scoreboard & Packet Listener (DuplexChannel)
            scoreboardManager.createScoreboard(player)
            PacketListeners.addPlayer(player)

            QuestsHandler(player)
        }
    }

    @EventHandler
    fun onPlayerLeave(e: PlayerQuitEvent) {
        val player: Player = e.player
        val uuid: UUID = player.uniqueId
        val map = PlayerRegistry.playerData

        e.quitMessage(null)

        runAsync {
            println("Removing data for $uuid")
            map[uuid]?.let {
                DataHandler.updateDataThenDelete(uuid)
                println("Removed data for $uuid")
            }

            // Remove Scoreboard & Packet Listener (DuplexChannel)
            scoreboardManager.removeScoreboard(player)
            PacketListeners.removePlayer(player)
        }
    }

    @EventHandler
    fun onLevelup(e: PlayerLevelChangeEvent) {
        val player: Player = e.player
        val oldLevel: Int = e.oldLevel
        val newLevel: Int = e.newLevel

        if (newLevel > oldLevel) {
            val title: Title =
                Title.title(
                    "<main><b>ʟᴇᴠᴇʟᴜᴘ</b>".parse(),
                    "<main>$oldLevel <gray>➲ <second>$newLevel".parse(),
                )

            player.showTitle(title)

            CustomSound.Levelup(pitch = 1.56F).playSound(player)
        }
    }
}
