package me.outspending.core.packets

import me.outspending.core.utils.Utilities.sendPacket
import net.minecraft.core.particles.ParticleOptions
import net.minecraft.network.protocol.game.ClientboundLevelParticlesPacket
import org.bukkit.Location
import org.bukkit.entity.Player

data class ClientboundParticlePacket<T : ParticleOptions>(
    private var location: Location,
    private var data: T
) : PacketWrapper {
    override fun sendPacket(receivingPlayer: Player) {
        receivingPlayer.sendPacket(
            ClientboundLevelParticlesPacket(
                data,
                false,
                location.x,
                location.y,
                location.z,
                0f,
                0f,
                0f,
                0f,
                1,
            ),
        )
    }
}
