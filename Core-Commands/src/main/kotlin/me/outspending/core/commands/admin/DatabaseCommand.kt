package me.outspending.core.commands.admin

import com.azuyamat.maestro.bukkit.annotations.Command
import com.azuyamat.maestro.bukkit.annotations.SubCommand
import me.outspending.core.core
import me.outspending.core.storage.registries.PlayerRegistry
import org.bukkit.command.CommandSender

@Command(
    name = "database",
    permission = "core.database",
)
class DatabaseCommand {
    @SubCommand("reload")
    fun reload(player: CommandSender) {
        PlayerRegistry.updateAllPlayerData()
        player.sendMessage(core.messageConfig.getMessage("commands.admin.database.reload_success"))
    }

    @SubCommand("test")
    fun test(player: CommandSender) {
        player.sendMessage(core.messageConfig.getMessage("commands.admin.database.test"))
    }
}
