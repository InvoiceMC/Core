package me.outspending.core.holograms

import me.outspending.core.helpers.FormatHelper.Companion.parse
import org.bukkit.Location
import org.bukkit.entity.Display
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.entity.TextDisplay

object Hologram {
    fun createServerSideHologram(location: Location, lines: Array<String>): TextDisplay =
        createServerSideHologram(location, lines.toList())

    fun createServerSideHologram(location: Location, lines: List<String>): TextDisplay {
        val entity = location.world.spawnEntity(location, EntityType.TEXT_DISPLAY) as TextDisplay
        entity.apply {
            this.billboard = Display.Billboard.CENTER
            this.text(lines.joinToString("\n").parse())
        }

        return entity
    }

    fun createClientSideHologram(
        receivingPlayer: Player,
        location: Location,
        lines: Array<String>
    ) = ClientboundHologramPacket(location, lines).sendPacket(receivingPlayer)

    fun createClientSideHologram(receivingPlayer: Player, location: Location, lines: List<String>) =
        ClientboundHologramPacket(location, lines).sendPacket(receivingPlayer)
}