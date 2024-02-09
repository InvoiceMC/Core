package me.outspending.core.mining.duplex

import me.outspending.core.Utilities.getConnection
import org.bukkit.entity.Player

object PacketListeners {
    fun addPlayer(player: Player) {
        val channel = player.getConnection()?.connection?.channel
        val pipeline = channel?.pipeline()

        pipeline?.addBefore(
            "packet_handler",
            player.name,
            MiningDuplexHandler(player, player.getConnection()!!)
        )
    }

    fun removePlayer(player: Player) {
        val channel = player.getConnection()?.connection?.channel
        channel?.eventLoop()?.submit { channel.pipeline()?.remove(player.name) }
    }
}
