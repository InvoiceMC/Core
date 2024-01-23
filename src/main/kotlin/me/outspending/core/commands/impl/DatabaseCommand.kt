package me.outspending.core.commands.impl

import me.outspending.core.commands.annotations.Command
import me.outspending.core.commands.annotations.SubCommand
import me.outspending.core.storage.DataHandler
import me.outspending.core.utils.helpers.FormatHelper.Companion.parse
import org.bukkit.command.CommandSender

@Command(
    name = "database",
    permission = "core.database",
)
class DatabaseCommand {

    fun onCommand(player: CommandSender) {
        player.sendMessage("<gray>Usage: <main>/database reload".parse(true))
    }

    @SubCommand("reload")
    fun reload(player: CommandSender) {
        DataHandler.updateAllPlayerData()
        player.sendMessage("<gray>Reloaded all player data!".parse(true))
    }
}
