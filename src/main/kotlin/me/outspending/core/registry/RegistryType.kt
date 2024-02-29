package me.outspending.core.registry

import me.outspending.core.commands.CommandRegistry
import me.outspending.core.listeners.ListenerRegistry

enum class RegistryType(private val registry: Registrable, private vararg val packages: String) {
    COMMANDS(CommandRegistry(), "me.outspending.core.commands"),
    LISTENERS(ListenerRegistry(), "me.outspending.core.listeners", "me.outspending.core.bot.listeners");

    fun register() = registry.register(*packages)
}