package me.outspending.core.commands.utility

import com.azuyamat.maestro.common.annotations.Command
import me.outspending.core.config.messagesConfig
import org.bukkit.entity.Player

@Command(
    name = "fly",
    description = "Toggle fly mode",
    aliases = ["flight"],
    permission = "core.fly",
)
class FlyCommand {

    fun onCommand(player: Player, target: Player?) {

        val target = target ?: player
        target.allowFlight = !target.allowFlight
        val status = if (target.allowFlight) "enabled" else "disabled"
        val color = if (target.allowFlight) "<green>" else "<red>"
        player.sendMessage(
            messagesConfig.getMessageWithArgs(
                "commands.utility.fly.success_self",
                true,
                color+status,
                target.name
            )
        )
        if (target != player) {
            target.sendMessage(
                messagesConfig.getMessageWithArgs(
                    "commands.utility.fly.success_other",
                    true,
                    color+status,
                    player.name
                )
            )
        }
    }
}