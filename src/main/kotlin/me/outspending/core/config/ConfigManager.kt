package me.outspending.core.config

import me.outspending.core.Core
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File

abstract class ConfigManager(name: String) {
    private val dataFolder = Core.instance.dataFolder
    private val configFile = File(dataFolder, "config/$name.yml")
    private var config = YamlConfiguration()

    fun load() {
        makeFileIfNotExists()
        config.load(configFile)
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

    fun getAllValues(): Map<String, Any> {
        return config.getValues(true)
    }

    private fun makeFileIfNotExists() {
        if (!configFile.exists()) {
            configFile.parentFile.mkdirs()
            configFile.createNewFile()
        }
    }
}