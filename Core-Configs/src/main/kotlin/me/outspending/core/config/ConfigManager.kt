package me.outspending.core.config

import me.outspending.core.CoreHandler.core
import me.outspending.core.config.impl.BroadcastsConfig
import me.outspending.core.config.impl.DiscordConfig
import me.outspending.core.config.impl.MessagesConfig
import me.outspending.core.config.impl.QuestsConfig
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import kotlin.time.measureTime

val broadcastConfig = BroadcastsConfig(core)
val discordConfig = DiscordConfig(core)
val messagesConfig = MessagesConfig(core)
val questsConfig = QuestsConfig(core)

abstract class ConfigManager(name: String, plugin: JavaPlugin) {
    private val dataFolder = plugin.dataFolder
    private val configFile = File(dataFolder, "config/$name.yml")
    private var config = YamlConfiguration()

    init {
        load()
    }

    private fun load() {
        val time = measureTime {
            makeFileIfNotExists()
            config.load(configFile)
        }
        println("[CONFIG] Loaded $configFile in $time!")
    }

    fun save() {
        config.save(configFile)
    }

    fun reload() {
        config.load(configFile)
    }

    fun setValue(key: String, value: Any) {
        config.set(key, value)
    }

    fun getValue(key: String): ConfigValue {
        return ConfigValue(config.get(key))
    }

    fun getValueOr(key: String, or: () -> Any): ConfigValue {
        return getValue(key).takeIf { !it.isNull() }
            ?: ConfigValue(or())
    }

    fun getValueOr(key: String, or: String): ConfigValue {
        return getValue(key).takeIf { !it.isNull() }
            ?: ConfigValue(or)
    }

    fun removeValue(key: String) {
        config.set(key, null)
    }

    fun getAllValues(): Map<String, Any> {
        return config.getValues(true)
    }

    fun getRawConfig(): YamlConfiguration {
        return config
    }

    private fun makeFileIfNotExists() {
        if (!configFile.exists()) {
            configFile.parentFile.mkdirs()
            configFile.createNewFile()
        }
    }
}