package me.outspending.core.data.player

import me.outspending.core.CoreHandler.core
import me.outspending.core.Utilities.runAsync
import me.outspending.core.data.DatabaseManager.database
import me.outspending.core.data.DatabaseManager.munchPlayerData
import me.outspending.core.data.PlayerRegistry
import me.outspending.core.helpers.FormatHelper.Companion.parse
import me.outspending.core.helpers.enums.CustomSound
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
                database.getData(munchPlayerData, uuid).thenAccept {
                    val playerData: PlayerData
                    if (it == null) {
                        playerData = PlayerData(uuid)

                        database.addData(munchPlayerData, playerData)
                    } else {
                        playerData = database.getData(munchPlayerData, uuid).get()!!
                    }

                    PlayerRegistry.addPlayer(uuid, playerData)
                }
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
            core.scoreboardHandler.createScoreboard(player)
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
            map[uuid]?.let {
                // Check's if the player has data inside the database, if so, update it, else, add
                // it
                database.getData(munchPlayerData, uuid).thenAccept {
                    it?.let { data -> database.updateData(munchPlayerData, data, uuid) }
                }

                // Remove the player's data from memory
                PlayerRegistry.removePlayer(uuid)
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
