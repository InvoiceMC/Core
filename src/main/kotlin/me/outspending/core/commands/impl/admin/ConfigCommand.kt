package me.outspending.core.commands.impl.admin

import com.azuyamat.maestro.bukkit.annotations.Command
import com.azuyamat.maestro.bukkit.annotations.SubCommand
import me.outspending.core.Core
import me.outspending.core.utils.helpers.FormatHelper.Companion.parse
import org.bukkit.command.CommandSender

@Command(
    name = "config",
    description = "Manage the <main>config",
    permission = "core.admin.config"
)
class ConfigCommand {

    fun onCommand(sender: CommandSender) {
        sender.sendMessage(Core.messageConfig.getMessage("commands.admin.config.main"))
    }

    @SubCommand(
        name = "reload",
        description = "Reload the <main>config",
        permission = "core.admin.config.reload"
    )
    fun reload(sender: CommandSender) {
        Core.messageConfig.reload()
        sender.sendMessage(Core.messageConfig.getMessage("commands.admin.config.reload_success"))
    }

    @SubCommand(
        name = "info",
        description = "Info from <main>config",
        permission = "core.admin.config.info"
    )
    fun info(sender: CommandSender) {
        val message = Core.messageConfig.getAllValues().entries.joinToString("\n") { "<main>${it.key}: <gray>${it.value}" }
        sender.sendMessage(message.parse(true))
    }
}