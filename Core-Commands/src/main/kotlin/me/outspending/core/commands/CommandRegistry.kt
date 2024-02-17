package me.outspending.core.commands

import com.azuyamat.maestro.bukkit.Maestro
import com.azuyamat.maestro.bukkit.data.CommandData
import me.outspending.core.CoreHandler.core

object CommandRegistry {
    private const val COMMANDS_PACKAGE = "me.outspending.core.commands"
    internal var commandsList: MutableList<CommandData> = mutableListOf()

    fun registerAll() {
        Maestro(core).apply {
            registerCommands(COMMANDS_PACKAGE)
            commandsList = commands
        }
    }
}