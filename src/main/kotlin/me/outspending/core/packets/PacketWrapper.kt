package me.outspending.core.packets

import org.bukkit.entity.Player

fun interface PacketWrapper {
    fun sendPacket(receivingPlayer: Player)
}
