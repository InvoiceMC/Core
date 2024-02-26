package me.outspending.core.broadcaster

import me.outspending.core.CoreHandler.core
import me.outspending.core.config.impl.BroadcastsConfig

object BroadcastHandler {
    init {
        start()
    }

    val broadcastManager = BroadcastManager()
    val broadcastsConfig = BroadcastsConfig(core)

    private fun start() {
        broadcastManager.registerFromConfig(broadcastsConfig)

        broadcastManager.start()
    }
}
