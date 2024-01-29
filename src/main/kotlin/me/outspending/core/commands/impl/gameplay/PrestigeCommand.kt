package me.outspending.core.commands.impl.gameplay

import com.azuyamat.maestro.bukkit.annotations.Command
import me.outspending.core.storage.data.PlayerData
import me.outspending.core.utils.Utilities.getData
import me.outspending.core.utils.helpers.FormatHelper.Companion.parse
import org.bukkit.entity.Player

@Command(
    name = "prestige",
    description = "Prestige!",
)
class PrestigeCommand {

    fun onCommand(player: Player) {
        val playerData: PlayerData? = player.getData()

        playerData?.let { data ->
            val level = player.level
            val levelAmount: Int = 100 + (25 * data.prestige)
            if (level < levelAmount) {
                val levelNeeded = levelAmount - level

                player.sendMessage(
                    "<gray>You cannot prestige yet! You need <main>${levelNeeded} <gray>more levels"
                        .parse(true)
                )
                return@let
            }

            player.level = 0
            player.exp = 0.0f
            playerData.prestige += 1

            player.sendMessage(
                "<gray>You have prestiged! You are now prestige <main>${data.prestige}"
                    .parse(true)
            )
        }
    }
}
