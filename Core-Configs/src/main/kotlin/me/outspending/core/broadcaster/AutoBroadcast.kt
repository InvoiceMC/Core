package me.outspending.core.broadcaster

import me.outspending.core.helpers.FormatHelper.Companion.parse
import org.bukkit.Bukkit

class AutoBroadcast(val customId: String, private vararg val messages: String) {
    private var protected: Boolean = true

    fun send() = messages.forEach { Bukkit.broadcast(it.parse()) }

    fun getMessages() = messages

    fun unProtect() {
        protected = false
    }

    fun isProtected() = protected
}
