package me.outspending.core.holograms

import io.papermc.paper.adventure.PaperAdventure
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.JoinConfiguration
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket
import net.minecraft.world.entity.Display
import net.minecraft.world.entity.Display.BillboardConstraints
import net.minecraft.world.entity.EntityType
import net.minecraft.world.phys.Vec3
import org.bukkit.Location
import org.bukkit.craftbukkit.v1_20_R3.CraftWorld
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftPlayer
import org.bukkit.entity.Player

class ClientboundHologramPacket internal constructor(private val location: Location, private val lines: Collection<Component>){
    private val craftWorld: CraftWorld = location.world as CraftWorld
    internal val entity: Display.TextDisplay = Display.TextDisplay(EntityType.TEXT_DISPLAY, craftWorld.handle)

    constructor(location: Location, lines: Array<Component>) : this(location, lines.toList())

    init {
        entity.apply {
            billboardConstraints = BillboardConstraints.VERTICAL
            text = PaperAdventure.asVanilla(Component.join(JoinConfiguration.newlines(), lines))
        }
    }

    private fun getConnection(player: Player) = (player as CraftPlayer).handle.connection

    fun updateData(player: Player) {
        val dataPacket = entity.entityData.nonDefaultValues?.let {
            ClientboundSetEntityDataPacket(entity.id, it)
        }

        if (dataPacket != null) {
            getConnection(player).send(dataPacket)
        }
    }

    fun sendPacket(receivingPlayer: Player) {
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

        val connection = getConnection(receivingPlayer)
        connection.send(addPacket)

        updateData(receivingPlayer)
    }
}