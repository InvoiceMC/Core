package me.outspending.core.packets.custom

import me.outspending.core.packets.PacketWrapper
import org.bukkit.Color
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftEntity
import org.bukkit.entity.Entity
import org.bukkit.entity.Player

data class ClientboundGlowColorPacket(
    private val entity: net.minecraft.world.entity.Entity,
    private val color: Color
) : PacketWrapper {
    constructor(entity: Entity, color: Color) : this((entity as CraftEntity).handle, color)

    override fun sendPacket(receivingPlayer: Player) {
        TODO("Not yet implemented")
    }
}
