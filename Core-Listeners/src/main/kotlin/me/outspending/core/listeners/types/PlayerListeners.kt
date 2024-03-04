package me.outspending.core.listeners.types

import com.github.shynixn.mccoroutine.bukkit.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import me.outspending.core.CoreHandler.core
import me.outspending.core.data.player.playerDataManager
import me.outspending.core.helpers.FormatHelper.Companion.parse
import me.outspending.core.helpers.enums.CustomSound
import me.outspending.core.mining.duplex.PacketListeners
import me.outspending.core.pmines.data.pmineDataManager
import me.outspending.core.pmines.getPmine
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
import org.bukkit.event.player.PlayerLoginEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class AsyncPlayerListeners : Listener {
    @EventHandler
    suspend fun onPlayerLogin(e: PlayerLoginEvent) {
        core.launch {
            val data = async(Dispatchers.IO) { playerDataManager.loadData(e.player) }.await()
            val pmineName = data.pmineName ?: ""

            async(Dispatchers.IO) { pmineDataManager.loadData(pmineName) }.await()
        }
    }

    @EventHandler
    suspend fun onPlayerLeave(e: PlayerQuitEvent) {
        val player = e.player

        core.launch {
            async(Dispatchers.IO) {
                    val pmine = player.getPmine()

                    playerDataManager.saveData(e.player)
                    pmineDataManager.saveData(pmine.getMineName())
                }
                .await()
        }
    }
}

class PlayerListeners : Listener {

    @EventHandler
    fun onPlayerJoin(e: PlayerJoinEvent) {
        val player: Player = e.player

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

        // Scoreboard & Packet Listener (DuplexChannel)
        scoreboardManager.createScoreboard(player)
        PacketListeners.addPlayer(player)

        QuestsHandler(player)
    }

    @EventHandler
    fun onPlayerLeave(e: PlayerQuitEvent) {
        val player: Player = e.player

        e.quitMessage(null)

        // Remove Scoreboard & Packet Listener (DuplexChannel)
        scoreboardManager.removeScoreboard(player)
        PacketListeners.removePlayer(player)
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
