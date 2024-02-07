package me.outspending.core.broadcaster

import me.outspending.core.Core
import org.bukkit.scheduler.BukkitRunnable

const val DELAY = 9000L
const val PERIOD = 9000L

class BroadcastManager {
    private val broadcasts: MutableList<AutoBroadcast> = mutableListOf()

    fun addBroadcast(vararg messages: String) {
        broadcasts.add(AutoBroadcast(*messages))
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
            .runTaskTimerAsynchronously(Core.instance, DELAY, PERIOD)
    }
}
