package me.outspending.core.broadcaster

import me.outspending.core.utils.helpers.FormatHelper.Companion.parse
import org.bukkit.Bukkit

class AutoBroadcast(private vararg val messages: String) {
    fun send() = messages.forEach { Bukkit.broadcast(it.parse()) }
}
