package me.outspending.core.commands.admin

import com.azuyamat.maestro.common.annotations.Command
import com.azuyamat.maestro.common.annotations.SubCommand
import me.outspending.core.config.messagesConfig
import me.outspending.core.data.player.playerDataManager
import org.bukkit.command.CommandSender

@Command(
    name = "database",
    permission = "core.database",
)
class DatabaseCommand {
    @SubCommand("reload")
    fun reload(player: CommandSender) {
        playerDataManager.saveAllData()

        player.sendMessage(messagesConfig.getMessage("commands.admin.database.reload_success"))
    }

    @SubCommand("test")
    fun test(player: CommandSender) {
        player.sendMessage(messagesConfig.getMessage("commands.admin.database.test"))
    }
}
