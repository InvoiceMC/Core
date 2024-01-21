package me.outspending.core.commands

import me.outspending.core.Core
import org.bukkit.Bukkit
import org.bukkit.command.CommandMap
import java.util.logging.Logger

class CommandRegistry {
    private val logger: Logger = Core.instance.logger
    private val commandMap: CommandMap = Bukkit.getCommandMap()

    fun registerCommands() {
        val commandList =
            listOf(
                PickaxeCMD(),
                FlyCMD(),
                DatabaseCMD(),
                PrestigeCMD(),
                EnchantCMD()
            )

        for (cmd in commandList) {
            commandMap.register(cmd.name, "core", cmd)
            logger.info("Registered Command: ${cmd.name}")
        }
    }
}
