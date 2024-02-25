package me.outspending.core.holograms

import io.papermc.paper.adventure.PaperAdventure
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.JoinConfiguration
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket
import net.minecraft.network.protocol.game.ClientboundTeleportEntityPacket
import org.bukkit.Location
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftPlayer
import org.bukkit.entity.Player

class PacketHologram(private val player: Player, location: Location, lines: Collection<Component>) : Hologram {
    private val hologramPacket = ClientboundHologramPacket(location, lines)

    private val teleportPacket by lazy { ClientboundTeleportEntityPacket(hologramPacket.entity) }
    private val killPacket by lazy { ClientboundRemoveEntitiesPacket(hologramPacket.entity.id) }

    private fun getConnection() = (player as CraftPlayer).handle.connection

    override fun teleport(location: Location) {
        hologramPacket.entity.setPos(location.x, location.y, location.z)

        val connection = getConnection()
        connection.send(teleportPacket)
    }

    override fun setLines(lines: List<Component>) {
        val vanillaComponent = Component.join(JoinConfiguration.newlines(), lines)
        hologramPacket.entity.text = PaperAdventure.asVanilla(vanillaComponent)

        hologramPacket.updateData(player)
    }

    override fun addLine(line: Component) {
        // TODO: Add line
    }

    fun send(player: Player) = hologramPacket.sendPacket(player)

    override fun kill() {
        val connection = getConnection()
        connection.send(killPacket)
    }
}