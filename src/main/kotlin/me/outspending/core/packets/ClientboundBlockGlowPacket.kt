package me.outspending.core.packets

import me.outspending.core.utils.Utilities.Companion.getConnection
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.monster.Shulker
import net.minecraft.world.phys.Vec3
import org.bukkit.Color
import org.bukkit.Location
import org.bukkit.craftbukkit.v1_20_R3.CraftWorld
import org.bukkit.entity.Player

data class ClientboundBlockGlowPacket(private val location: Location, private val color: Color) :
    PacketWrapper {
    private val serverLevel: ServerLevel = (location.world as CraftWorld).handle
    private val entity = Shulker(EntityType.SHULKER, serverLevel)

    init {
        entity.setGlowingTag(true)
        entity.isInvisible = true
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
                EntityType.SHULKER,
                0,
                Vec3.ZERO,
                0.0,
            )
        val updatePacket =
            entity.entityData.nonDefaultValues?.let {
                ClientboundSetEntityDataPacket(entity.id, it)
            }

        receivingPlayer.getConnection()?.let { connection ->
            connection.send(addPacket)
            updatePacket?.let { connection.send(it) }

            ClientboundGlowColorPacket(entity, color).sendPacket(receivingPlayer)
        }
    }
}
