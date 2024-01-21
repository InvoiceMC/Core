package me.outspending.core.commands

import me.outspending.core.storage.DataHandler
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

class DatabaseCMD : Command("database") {
    override fun execute(player: CommandSender, p1: String, args: Array<out String>?): Boolean {
        val subCommand = args?.getOrNull(0)
        when (subCommand) {
            "reload" -> {
                DataHandler.updateAllPlayerData()
                return true
            }
            else -> {
                player.sendMessage("Unknown subcommand: $subCommand")
                return false
            }
        }
    }
}
