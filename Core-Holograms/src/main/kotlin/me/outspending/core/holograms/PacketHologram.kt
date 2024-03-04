package me.outspending.core.holograms

import io.papermc.paper.adventure.PaperAdventure
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.JoinConfiguration
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket
import net.minecraft.network.protocol.game.ClientboundTeleportEntityPacket
import org.bukkit.Location
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftPlayer
import org.bukkit.entity.Display
import org.bukkit.entity.Player

typealias LocalHologram = PacketHologram

class PacketHologram(location: Location, private var lines: MutableList<Component>) : Hologram {
    constructor(location: Location, lines: Collection<Component>) : this(location, lines.toMutableList())
    constructor(location: Location, vararg lines: String): this(location, lines.map { Component.text(it) })

    private val hologramPacket = ClientboundHologramPacket(location, lines)

    private val teleportPacket by lazy { ClientboundTeleportEntityPacket(hologramPacket.entity) }
    private val killPacket by lazy { ClientboundRemoveEntitiesPacket(hologramPacket.entity.id) }

    private fun getConnection(player: Player) = (player as CraftPlayer).handle.connection

    fun teleport(player: Player, location: Location) {
        hologramPacket.entity.setPos(location.x, location.y, location.z)

        val connection = getConnection(player)
        connection.send(teleportPacket)
    }

    override fun teleport(location: Location) {
        throw UnsupportedOperationException("Use teleport(player: Player, location: Location) instead")
    }

    fun updateLines(player: Player) {
        val vanillaComponent = PaperAdventure.asVanilla(Component.join(JoinConfiguration.newlines(), lines))
        hologramPacket.entity.text = vanillaComponent

        hologramPacket.updateData(player)
    }

    override fun updateLines() {
        throw UnsupportedOperationException("Use updateLines(player: Player) instead")
    }

    override fun setLines(lines: List<Component>) {
        this.lines = lines.toMutableList()
        updateLines()
    }

    override fun addLine(line: Component) {
        lines.add(line)
        updateLines()
    }

    override fun updateLine(index: Int, line: Component) {
        lines[index] = line
        updateLines()
    }

    override fun removeLine(index: Int) {
        lines.removeAt(index)
        updateLines()
    }

    fun send(player: Player) = hologramPacket.sendPacket(player)

    fun kill(player: Player) {
        val connection = getConnection(player)
        connection.send(killPacket)
    }

    override fun kill() {
        throw UnsupportedOperationException("Use kill(player: Player) instead")
    }
}