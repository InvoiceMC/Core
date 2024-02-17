package me.outspending.core.commands

import com.azuyamat.maestro.bukkit.Maestro
import me.outspending.core.CoreHandler.core

object CommandRegistry {
    private const val COMMANDS_PACKAGE = "me.outspending.core.commands"

    fun registerAll() {
        Maestro(core).apply {
            registerCommands(COMMANDS_PACKAGE)
        }
    }
}