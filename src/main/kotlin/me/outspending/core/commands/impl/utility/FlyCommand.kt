package me.outspending.core.commands.impl.utility

import com.azuyamat.maestro.bukkit.annotations.Command
import me.outspending.core.core
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
            core.messageConfig.getMessageWithArgs(
                "commands.utility.fly.success_self",
                true,
                color+status,
                target.name
            )
        )
        if (target != player) {
            target.sendMessage(
                core.messageConfig.getMessageWithArgs(
                    "commands.utility.fly.success_other",
                    true,
                    color+status,
                    player.name
                )
            )
        }
    }
}