package me.outspending.core.config.impl

import me.outspending.core.config.ConfigManager
import me.outspending.core.utils.helpers.FormatHelper.Companion.parse
import org.bukkit.plugin.java.JavaPlugin

class MessagesConfig(plugin: JavaPlugin): ConfigManager("messages", plugin) {
    fun getMessage(key: String, withPrefix: Boolean = false) =
        getValue(key).asFormattedMessage(withPrefix)
    fun getMessageOr(key: String, withPrefix: Boolean = false, or: String = "") =
        (getValue(key).takeIf { !it.isNull() }?.asString() ?: or).parse(withPrefix)
    fun getMessageWithArgs(key: String, withPrefix: Boolean, vararg args: Any) =
        getValue(key).parseArgs(*args).parse(withPrefix)
}