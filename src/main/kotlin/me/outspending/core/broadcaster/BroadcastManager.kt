package me.outspending.core.broadcaster

import me.outspending.core.config.impl.BroadcastsConfig
import me.outspending.core.core
import org.bukkit.scheduler.BukkitRunnable

const val DELAY = 9000L
const val PERIOD = 9000L
const val BROADCAST_INDENTATION_CHAR = "%line%"

class BroadcastManager {
    val broadcasts: MutableList<AutoBroadcast> = mutableListOf()

    fun addBroadcast(customId: String, vararg messages: String) {
        broadcasts.add(
            AutoBroadcast(customId, *messages)
        )
    }

    fun registerFromConfig(config: BroadcastsConfig) {
        config.getBroadcasts().forEach { broadcast ->
            val messages = broadcast.value.split(BROADCAST_INDENTATION_CHAR).map { it.trim().replace(BROADCAST_INDENTATION_CHAR, "") }.toTypedArray()
            println("Registering broadcast with custom id: ${broadcast.key}\nMessages: ${messages.joinToString()}")
            broadcasts.add(
                AutoBroadcast(broadcast.key, *messages).apply {
                    unProtect()
                }
            )
        }
    }

    fun clearBroadcasts() {
        broadcasts.removeIf{ !it.isProtected() }
    }

    fun removeBroadcastWithCustomId(customId: String) {
        broadcasts.removeIf { it.customId.equals(customId, ignoreCase = true) }
    }

    fun start() {
        object : BukkitRunnable() {
                private var currentIndex: Int = 0

                override fun run() {
                    if (currentIndex >= broadcasts.size) {
                        currentIndex = 0
                    }

                    broadcasts[currentIndex].send()
                    currentIndex += 1
                }
            }
            .runTaskTimerAsynchronously(core, DELAY, PERIOD)
    }
}
