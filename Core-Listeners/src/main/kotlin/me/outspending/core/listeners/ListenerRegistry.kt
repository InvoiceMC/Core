package me.outspending.core.listeners

import me.outspending.core.CoreHandler.core
import me.outspending.core.registry.Registrable
import org.bukkit.event.Listener
import org.reflections.Reflections

class ListenerRegistry : Registrable {
    override fun register(vararg packages: String) {
        Reflections(packages)
            .getSubTypesOf(Listener::class.java)
            .forEach {
                val listener = it.getDeclaredConstructor().newInstance() as Listener
                core.server.pluginManager.registerEvents(listener, core)
            }
    }
}