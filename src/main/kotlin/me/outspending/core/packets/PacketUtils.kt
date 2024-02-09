package me.outspending.core.packets

import org.bukkit.Bukkit
import org.bukkit.entity.Player

object PacketUtils {

    fun broadcastCustomPacket(packet: PacketWrapper) =
        Bukkit.getOnlinePlayers().forEach { packet.sendPacket(it) }

    inline fun broadcastCustomPacket(
        packet: PacketWrapper,
        filter: (Player) -> Boolean,
    ) =
        Bukkit.getOnlinePlayers()
            .filter(
                filter,
            )
            .forEach { packet.sendPacket(it) }
}
