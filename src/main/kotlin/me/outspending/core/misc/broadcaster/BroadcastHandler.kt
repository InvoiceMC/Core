package me.outspending.core.misc.broadcaster

import me.outspending.core.config.impl.BroadcastsConfig
import me.outspending.core.core

object BroadcastHandler {
    val broadcastManager = BroadcastManager()
    val broadcastsConfig = BroadcastsConfig(core)

    init {
        broadcastsConfig.load()
    }

    fun registerAllBroadcasts() {
        broadcastManager.addBroadcast("Welcome", "Epicness")
        broadcastManager.addBroadcast("Welcome1", "Epicness1")
        broadcastManager.addBroadcast("Welcome2", "Epicness2")
        broadcastManager.addBroadcast("Welcome3", "Epicness3")
        broadcastManager.registerFromConfig(broadcastsConfig)

        broadcastManager.start()
    }
}
