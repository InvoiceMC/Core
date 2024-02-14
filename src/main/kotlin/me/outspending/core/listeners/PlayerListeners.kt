package me.outspending.core.listeners

import me.outspending.core.Utilities.runAsync
import me.outspending.core.core
import me.outspending.core.mining.duplex.PacketListeners
import me.outspending.core.misc.helpers.FormatHelper.Companion.parse
import me.outspending.core.misc.helpers.enums.CustomSound
import me.outspending.core.storage.DatabaseManager.database
import me.outspending.core.storage.data.PlayerData
import me.outspending.core.storage.registries.PlayerRegistry
import net.kyori.adventure.text.Component
import net.kyori.adventure.title.Title
import net.md_5.bungee.api.ChatColor
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerLevelChangeEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import java.net.URL
import java.util.*
import javax.imageio.ImageIO
import kotlin.time.measureTime

class PlayerListeners : Listener {
    private fun getSkin(player: Player, symbol: Char = '█'): MutableList<String> {
        val skin: MutableList<String> = mutableListOf()
        val image =
            ImageIO.read(URL("https://crafatar.com/avatars/${player.uniqueId}?size=8&overlay"))
        for (x in 0 until 8) {
            var currentLine = ""
            for (y in 0 until 8) {
                val color = ChatColor.of(java.awt.Color(image.getRGB(y, x)))
                currentLine += "$color$symbol"
            }

            skin.add(currentLine)
        }

        return skin
    }

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
                database.getData(uuid).thenAccept {
                    val playerData: PlayerData
                    if (it == null) {
                        playerData = PlayerData(uuid)

                        database.addData(playerData)
                    } else {
                        playerData = database.getData(uuid).get()!!
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
                database.getData(uuid).thenAccept {
                    it?.let { data -> database.updateData(data, uuid) }
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
