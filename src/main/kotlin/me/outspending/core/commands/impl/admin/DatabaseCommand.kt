package me.outspending.core.commands.impl.admin

import com.azuyamat.maestro.bukkit.annotations.Command
import com.azuyamat.maestro.bukkit.annotations.SubCommand
import me.outspending.core.messageConfig
import me.outspending.core.storage.DataHandler
import me.outspending.core.utils.helpers.FormatHelper.Companion.parse
import org.bukkit.command.CommandSender

@Command(
    name = "database",
    permission = "core.database",
)
class DatabaseCommand {

    fun onCommand(player: CommandSender) {
        player.sendMessage(messageConfig.getValue("commands.admin.database.main").asFormattedMessage(true))
    }

    @SubCommand("reload")
    fun reload(player: CommandSender) {
        DataHandler.updateAllPlayerData()
        player.sendMessage(messageConfig.getValue("commands.admin.database.reload_success").asFormattedMessage(true))
    }

    @SubCommand("test")
    fun test(player: CommandSender) {
        player.sendMessage(messageConfig.getValue("commands.admin.database.test").asFormattedMessage(true))
    }
}
