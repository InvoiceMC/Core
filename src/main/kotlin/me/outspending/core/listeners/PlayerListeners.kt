package me.outspending.core.listeners

import me.outspending.core.Core
import me.outspending.core.storage.DataHandler
import me.outspending.core.storage.data.PlayerData
import me.outspending.core.utils.Utilities.runAsync
import me.outspending.core.utils.Utilities.toComponent
import net.kyori.adventure.key.Key
import net.kyori.adventure.sound.Sound
import net.kyori.adventure.text.Component
import net.kyori.adventure.title.Title
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerLevelChangeEvent
import org.bukkit.event.player.PlayerQuitEvent
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
                    .toComponent()
            } else {
                "<gray>${player.name} has joined!".toComponent()
            }
        e.joinMessage(joinMessage)

        // Load data
        runAsync {
            val time = measureTime {
                val playerData = Core.playerDatabase.getData(uuid) ?: PlayerData.default()
                DataHandler.addPlayer(uuid, playerData)
            }

            player.showTitle(
                Title.title(
                    "<gradient:gold:white>★★★</gradient> <#e08a19><b>DATABASE</b> <gradient:white:gold>★★★</gradient>"
                        .toComponent(),
                    "<gray><i>Finished loading your data in <#e8b36d><u>$time</u><gray>!"
                        .toComponent(),
                ),
            )

            player.playSound(
                Sound.sound(
                    Key.key("minecraft", "entity.experience_orb.pickup"),
                    Sound.Source.PLAYER,
                    1.0f,
                    1.65f,
                ),
            )

            Core.scoreboardHandler.createScoreboard(player)
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
                val database = Core.playerDatabase
                if (database.hasData(uuid)) {
                    database.updateData(uuid, it)
                } else {
                    database.createData(uuid, it)
                }

                DataHandler.removePlayer(uuid)
            }

            Core.scoreboardHandler.removeScoreboard(player)
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
                    "<bold><gradient:gold:white>★★★</gradient> <#e8b36d>LEVELUP <gradient:white:gold>★★★</gradient>"
                        .toComponent(),
                    "<#e08a19>$oldLevel <gray>➲ <#e8b36d>$newLevel".toComponent(),
                )

            player.showTitle(title)
            player.playSound(
                Sound.sound(
                    Key.key("minecraft", "entity.player.levelup"),
                    Sound.Source.PLAYER,
                    1.0f,
                    1.56f,
                ),
            )
        }
    }
}