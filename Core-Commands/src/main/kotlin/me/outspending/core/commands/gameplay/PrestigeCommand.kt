package me.outspending.core.commands.gameplay

import com.azuyamat.maestro.common.annotations.Command
import me.outspending.core.config.messagesConfig
import me.outspending.core.data.getData
import me.outspending.core.data.player.PlayerData
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
                    messagesConfig.getMessageWithArgs(
                        "commands.gameplay.prestige.cannot_prestige",
                        true,
                        levelNeeded
                    )
                )
                return@let
            }

            player.level = 0
            player.exp = 0.0f
            playerData.prestige += 1

            player.sendMessage(
                messagesConfig.getMessageWithArgs(
                    "commands.gameplay.prestige.success",
                    true,
                    data.prestige
                )
            )
        }
    }
}
