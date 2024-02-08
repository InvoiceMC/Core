package me.outspending.core.commands.impl.admin

import com.azuyamat.maestro.bukkit.annotations.Command
import com.azuyamat.maestro.bukkit.annotations.SubCommand
import me.outspending.core.Core
import me.outspending.core.core
import me.outspending.core.storage.DataHandler
import org.bukkit.command.CommandSender

@Command(
    name = "database",
    permission = "core.database",
)
class DatabaseCommand {

    fun onCommand(player: CommandSender) {
        player.sendMessage(core.messageConfig.getMessage("commands.admin.database.main"))
    }

    @SubCommand("reload")
    fun reload(player: CommandSender) {
        DataHandler.updateAllPlayerData()
        player.sendMessage(core.messageConfig.getMessage("commands.admin.database.reload_success"))
    }

    @SubCommand("test")
    fun test(player: CommandSender) {
        player.sendMessage(core.messageConfig.getMessage("commands.admin.database.test"))
    }
}
