package me.outspending.core.commands.admin

import com.azuyamat.maestro.bukkit.annotations.Command
import com.azuyamat.maestro.bukkit.annotations.SubCommand
import me.outspending.core.config.messagesConfig
import me.outspending.core.config.questsConfig
import me.outspending.core.helpers.FormatHelper.Companion.parse
import org.bukkit.command.CommandSender

@Command(
    name = "config",
    description = "Manage the <main>config",
    permission = "core.admin.config"
)
class ConfigCommand {
    @SubCommand(
        name = "reload",
        description = "Reload the <main>config",
        permission = "core.admin.config.reload"
    )
    fun reload(sender: CommandSender) {
        messagesConfig.reload()
        questsConfig.reload()
        sender.sendMessage(messagesConfig.getMessage("commands.admin.config.reload_success"))
    }

    @SubCommand(
        name = "info",
        description = "Info from <main>config",
        permission = "core.admin.config.info"
    )
    fun info(sender: CommandSender) {
        val message = messagesConfig.getAllValues().entries.joinToString("\n") { "<main>${it.key}: <gray>${it.value}" }
        sender.sendMessage(message.parse(true))
    }
}