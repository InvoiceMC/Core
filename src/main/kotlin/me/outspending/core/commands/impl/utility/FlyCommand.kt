package me.outspending.core.commands.impl.utility

import com.azuyamat.maestro.bukkit.annotations.Command
import me.outspending.core.messageConfig
import me.outspending.core.utils.helpers.FormatHelper.Companion.parse
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
            messageConfig.getValue("commands.utility.fly.success_self").parseArgs(
                color+status,
                target.name
            ).parse(true)
        )
        if (target != player) {
            target.sendMessage(
                messageConfig.getValue("commands.utility.fly.success_other").parseArgs(
                    color+status,
                    player.name
                ).parse(true)
            )
        }
    }
}