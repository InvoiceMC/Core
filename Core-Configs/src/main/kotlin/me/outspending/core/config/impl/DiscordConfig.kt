package me.outspending.core.config.impl

import me.outspending.core.config.ConfigManager
import org.bukkit.plugin.java.JavaPlugin

class DiscordConfig(plugin: JavaPlugin): ConfigManager("discord", plugin) {
    fun getToken() = getValue("token").asString()
    fun getLogChannelId() = getValue("log-channel").asString()
    fun getMsgChannelId() = getValue("msg-channel").asString()
    fun getGuildId() = getValue("guild").asString()
}