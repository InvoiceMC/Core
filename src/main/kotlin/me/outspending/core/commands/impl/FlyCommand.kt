package me.outspending.core.commands.impl

import me.outspending.core.commands.annotations.Catcher
import me.outspending.core.commands.annotations.Command
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
        player.sendMessage("<gray>Flight $color$status <gray>for <main>${target.name}".parse(true))
        if (target != player) {
            target.sendMessage("<gray>Your flight was $color$status <gray>by <main>${player.name}".parse(true))
        }
    }
}