package me.outspending.core.utils

import me.outspending.core.packets.PacketWrapper
import org.bukkit.Bukkit
import org.bukkit.entity.Player

object PacketUtils {
    @JvmStatic
    fun broadcastCustomPacket(packet: PacketWrapper) =
        Bukkit.getOnlinePlayers().forEach { packet.sendPacket(it) }

    @JvmStatic
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
