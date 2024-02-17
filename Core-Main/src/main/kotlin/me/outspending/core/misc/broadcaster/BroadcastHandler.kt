package me.outspending.core.misc.broadcaster

import me.outspending.core.config.impl.BroadcastsConfig
import me.outspending.core.core

object BroadcastHandler {
    val broadcastManager = BroadcastManager()
    val broadcastsConfig = BroadcastsConfig(core)

    fun registerAllBroadcasts() {
        broadcastManager.registerFromConfig(broadcastsConfig)

        broadcastManager.start()
    }
}
