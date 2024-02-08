package me.outspending.core.config.impl

import me.outspending.core.config.ConfigManager
import org.bukkit.plugin.java.JavaPlugin

class BroadcastsConfig(plugin: JavaPlugin): ConfigManager("broadcasts", plugin) {
    fun getBroadcasts(): Map<String, String> = getAllValues().map { it.key to it.value.toString() }.toMap()
}