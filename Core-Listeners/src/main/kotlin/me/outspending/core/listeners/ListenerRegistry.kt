package me.outspending.core.listeners

import me.outspending.core.CoreHandler.core
import org.bukkit.event.Listener
import org.reflections.Reflections

object ListenerRegistry {
    private val LISTENER_PACKAGES: Array<String> =
        arrayOf("me.outspending.core.listeners.types", "me.outspending.core.bot.listeners")

    fun registerEvents() {
        val manager = core.server.pluginManager
        for (pkg in LISTENER_PACKAGES) {
            Reflections(pkg).getSubTypesOf(Listener::class.java).forEach {
                val listener = it.getDeclaredConstructor().newInstance() as Listener

                manager.registerEvents(listener, core)
            }
        }
    }
}