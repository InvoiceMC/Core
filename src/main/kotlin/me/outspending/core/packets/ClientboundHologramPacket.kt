package me.outspending.core.packets

import me.outspending.core.utils.Utilities.Companion.colorizeHex
import me.outspending.core.utils.Utilities.Companion.getConnection
import net.minecraft.network.chat.Component
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket
import net.minecraft.world.entity.Display
import net.minecraft.world.entity.Display.TextDisplay
import net.minecraft.world.entity.EntityType
import net.minecraft.world.phys.Vec3
import org.bukkit.Location
import org.bukkit.craftbukkit.v1_20_R3.CraftWorld
import org.bukkit.entity.Player

class ClientboundHologramPacket(private val location: Location, private val lines: List<String>) :
    PacketWrapper {
    private val craftWorld: CraftWorld = location.world as CraftWorld
    private val entity: TextDisplay = TextDisplay(EntityType.TEXT_DISPLAY, craftWorld.handle)

    constructor(location: Location, lines: Array<String>) : this(location, lines.toList())

    init {
        entity.apply {
            billboardConstraints = Display.BillboardConstraints.CENTER
            text = Component.nullToEmpty(lines.joinToString("\n") { it.colorizeHex() })
        }
    }

    override fun sendPacket(receivingPlayer: Player) {
        val addPacket =
            ClientboundAddEntityPacket(
                entity.id,
                entity.uuid,
                location.x,
                location.y,
                location.z,
                0f,
                0f,
                EntityType.TEXT_DISPLAY,
                0,
                Vec3.ZERO,
                0.0,
            )

        val dataPacket =
            entity.entityData.nonDefaultValues?.let {
                ClientboundSetEntityDataPacket(entity.id, it)
            }

        receivingPlayer.getConnection()?.let { connection ->
            connection.send(addPacket)
            dataPacket?.let { connection.send(it) }
        }
    }
}
