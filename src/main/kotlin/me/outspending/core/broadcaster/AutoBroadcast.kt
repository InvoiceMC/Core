package me.outspending.core.broadcaster

import me.outspending.core.utils.Utilities.Companion.toComponent
import org.bukkit.Bukkit

class AutoBroadcast(vararg val messages: String) {
    fun send() = messages.forEach { Bukkit.broadcast(it.toComponent()) }
}
