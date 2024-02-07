package me.outspending.core.config

import me.outspending.core.utils.helpers.FormatHelper

class ConfigValue(private val value: Any?) {
    fun asRaw() = value
    fun asString() = value.toString()
    fun asInt() = value.toString().toInt()
    fun asDouble() = value.toString().toDouble()
    fun asBoolean() = value.toString().toBoolean()
    fun asFormattedMessage(withPrefix: Boolean = false) = FormatHelper(asString()).parse(withPrefix)
    fun parseArgs(vararg args: Any): String {
        var parsed = asString()
        for (i in args.indices) {
            parsed = parsed.replace("%${i + 1}", args[i].toString())
        }
        return parsed
    }
    fun isNull() = value == null
}