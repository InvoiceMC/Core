package me.outspending.core.listeners

import me.outspending.core.core
import org.bukkit.event.Listener
import org.bukkit.plugin.PluginManager
import org.reflections.Reflections

const val LISTENERS_PACKAGE = "me.outspending.core.listeners"
const val LISTENERS_PACKAGE_DISCORD = "me.outspending.core.bot.listeners.minecraft"

object ListenerRegistry {
    fun registerEvents(pluginManager: PluginManager) {
        Reflections(LISTENERS_PACKAGE).getSubTypesOf(Listener::class.java).forEach {
            pluginManager.registerEvents(it.newInstance(), core)
        }
        Reflections(LISTENERS_PACKAGE_DISCORD).getSubTypesOf(Listener::class.java).forEach {
            pluginManager.registerEvents(it.newInstance(), core)
        }
    }
}
