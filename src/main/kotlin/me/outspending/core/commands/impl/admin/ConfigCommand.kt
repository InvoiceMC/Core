package me.outspending.core.commands.impl.admin

import com.azuyamat.maestro.bukkit.annotations.Command
import com.azuyamat.maestro.bukkit.annotations.SubCommand
import me.outspending.core.messageConfig
import me.outspending.core.utils.helpers.FormatHelper.Companion.parse
import org.bukkit.command.CommandSender

@Command(
    name = "config",
    description = "Manage the <main>config",
    permission = "core.admin.config"
)
class ConfigCommand {

    fun onCommand(sender: CommandSender) {
        sender.sendMessage(messageConfig.getValue("commands.admin.config.main").asString().parse(true))
    }

    @SubCommand(
        name = "reload",
        description = "Reload the <main>config",
        permission = "core.admin.config.reload"
    )
    fun reload(sender: CommandSender) {
        messageConfig.reload()
        sender.sendMessage(messageConfig.getValue("commands.admin.config.reload_success").asString().parse(true))
    }

    @SubCommand(
        name = "info",
        description = "Info from <main>config",
        permission = "core.admin.config.info"
    )
    fun info(sender: CommandSender) {
        val message = messageConfig.getAllValues().entries.joinToString("\n") { "<main>${it.key}: <gray>${it.value}" }
        sender.sendMessage(message.parse(true))
    }
}