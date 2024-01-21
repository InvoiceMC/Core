package me.outspending.core.commands

import me.outspending.core.utils.Utilities.Companion.toComponent
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class FlyCMD : Command("fly") {
    override fun execute(player: CommandSender, p1: String, p2: Array<out String>?): Boolean {
        if (player is Player) {
            val flight = player.allowFlight
            player.allowFlight = !flight

            player.sendMessage(
                "<gray>Toggled your fly to ${if (!flight) "<green>On" else "<red>Off"}"
                    .toComponent()
            )
            return true
        }

        return false
    }
}
