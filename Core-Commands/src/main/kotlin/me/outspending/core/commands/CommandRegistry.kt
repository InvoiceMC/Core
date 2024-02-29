package me.outspending.core.commands

import com.azuyamat.maestro.bukkit.BukkitMaestro
import com.azuyamat.maestro.common.data.command.CommandData
import me.outspending.core.CoreHandler.core
import me.outspending.core.registry.Registrable

class CommandRegistry : Registrable {
    internal var commandsList: MutableList<CommandData> = mutableListOf()

    override fun register(vararg packages: String) {
        BukkitMaestro(core).apply {
            registerCommands(*packages)
            commandsList = commands
        }
    }

}