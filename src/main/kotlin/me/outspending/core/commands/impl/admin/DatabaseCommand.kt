package me.outspending.core.commands.impl.admin

import com.azuyamat.maestro.bukkit.annotations.Command
import com.azuyamat.maestro.bukkit.annotations.SubCommand
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

    @SubCommand("test")
    fun test(player: CommandSender) {
        player.sendMessage("<gray>Test!".parse(true))
    }
}
