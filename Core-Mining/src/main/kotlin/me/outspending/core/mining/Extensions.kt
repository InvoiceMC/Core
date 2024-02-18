package me.outspending.core.mining

import net.minecraft.network.protocol.Packet
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftPlayer
import org.bukkit.entity.Player

object Extensions {
    fun Player.getConnection() = (this as CraftPlayer).handle.connection
}