package me.outspending.core.listeners

import me.outspending.core.core
import me.outspending.core.mining.pickaxe.PickaxeListener
import org.bukkit.plugin.PluginManager

object ListenerHandler {
    fun registerEvents(pluginManager: PluginManager) {
        listOf(
            ChatListeners(),
            CommandListeners(),
            MiscListeners(),
            PlayerListeners(),
            PickaxeListener()
        )
            .map { pluginManager.registerEvents(it, core) }
    }
}