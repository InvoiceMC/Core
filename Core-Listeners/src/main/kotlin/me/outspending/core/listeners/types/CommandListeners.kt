package me.outspending.core.listeners.types

import me.outspending.core.helpers.FormatHelper.Companion.parse
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerCommandPreprocessEvent

class CommandListeners : Listener {
    private val BLACKLISTED_COMMANDS: List<String> =
        listOf(
            "plugins",
            "pl",
            "bukkit:plugins",
            "help",
            "?",
            "bukkit:help",
            "bukkit:?",
            "tell",
            "me",
            "minecraft:tell",
            "minecraft:me",
            "version",
            "ver",
            "about",
            "bukkit:about",
            "say",
            "icanhasbukkit"
        )

    @EventHandler
    fun onCommand(e: PlayerCommandPreprocessEvent) {
        val player: Player = e.player
        val commandName: String = e.message.split(" ")[0].substring(1)

        if (player.hasPermission("core.bypassCommands")) return
        if (BLACKLISTED_COMMANDS.contains(commandName)) {
            e.isCancelled = true
            e.player.sendMessage("Unknown command. Type \"/help\" for help.".parse())
        }
    }
}