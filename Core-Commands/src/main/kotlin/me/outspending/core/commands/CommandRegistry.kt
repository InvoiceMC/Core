package me.outspending.core.commands

import com.azuyamat.maestro.bukkit.BukkitMaestro
import com.azuyamat.maestro.common.data.command.CommandData
import me.outspending.core.CoreHandler.core

object CommandRegistry {
    private const val COMMANDS_PACKAGE = "me.outspending.core.commands"
    internal var commandsList: MutableList<CommandData> = mutableListOf()

    fun registerAll() {
        BukkitMaestro(core).apply {
            registerCommands(COMMANDS_PACKAGE)
            commandsList = commands
        }
    }
}