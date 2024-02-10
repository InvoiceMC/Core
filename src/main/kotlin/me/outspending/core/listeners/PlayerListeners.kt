package me.outspending.core.listeners

import me.outspending.core.Utilities.runAsync
import me.outspending.core.core
import me.outspending.core.mining.duplex.PacketListeners
import me.outspending.core.misc.helpers.FormatHelper.Companion.parse
import me.outspending.core.misc.helpers.enums.CustomSound
import me.outspending.core.storage.DataHandler
import me.outspending.core.storage.DatabaseHandler.database
import me.outspending.core.storage.DatabaseHandler.munchPlayerData
import me.outspending.core.storage.data.PlayerData
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
                "<#e08a19>${player.name} <white>has joined for the first time! <gray>[<#e8b36d>#${Bukkit.getOfflinePlayers().size}<gray>]"
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
                val playerData = database.getData(munchPlayerData, uuid) ?: PlayerData(uuid)
                DataHandler.addPlayer(uuid, playerData)
            }

            // Show data loaded title
            player.showTitle(
                Title.title(
                    "<main><b>ᴅᴀᴛᴀʙᴀꜱᴇ</b>".parse(),
                    "<gray><i>Loaded your data in <main><u>$time</u><gray>!".parse(),
                ),
            )

            // Play sound
            CustomSound.Success(pitch = 1.65F).playSound(player)

            // Scoreboard & Packet Listener (DuplexChannel)
            core.scoreboardHandler.createScoreboard(player)
            PacketListeners.addPlayer(player)
        }
    }

    @EventHandler
    fun onPlayerLeave(e: PlayerQuitEvent) {
        val player: Player = e.player
        val uuid: UUID = player.uniqueId
        val map = DataHandler.playerData

        e.quitMessage(null)

        runAsync {
            map[uuid]?.let {
                // Check's if the player has data inside the database, if so, update it, else, add it
                val hasData = database.hasData(munchPlayerData, uuid) ?: false
                if (hasData) {
                    database.updateData(munchPlayerData, it, uuid)
                } else {
                    database.addData(munchPlayerData, it)
                }

                // Remove the player's data from memory
                DataHandler.removePlayer(uuid)
            }

            // Remove Scoreboard & Packet Listener (DuplexChannel)
            core.scoreboardHandler.removeScoreboard(player)
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
