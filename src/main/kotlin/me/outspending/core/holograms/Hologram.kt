package me.outspending.core.holograms

import me.outspending.core.packets.ClientboundHologramPacket
import me.outspending.core.utils.DisplayUtils
import me.outspending.core.utils.PacketUtils
import org.bukkit.Location
import org.bukkit.entity.Player

class Hologram {
    companion object {
        fun createServerSideHologram(
            location: Location,
            lines: Array<String>,
        ) = createServerSideHologram(location, lines.toList())

        fun createServerSideHologram(
            location: Location,
            lines: List<String>,
        ) = DisplayUtils.spawnTextDisplay(location, lines)

        fun createClientSideHologram(
            player: Player,
            location: Location,
            lines: Array<String>,
        ) =
            ClientboundHologramPacket(
                    location,
                    lines,
                )
                .sendPacket(player)

        fun broadcastClientSideHologram(
            location: Location,
            lines: Array<String>,
        ) = PacketUtils.broadcastCustomPacket(ClientboundHologramPacket(location, lines))

        inline fun broadcastClientSideHologram(
            location: Location,
            lines: Array<String>,
            filter: (Player) -> Boolean,
        ) =
            PacketUtils.broadcastCustomPacket(ClientboundHologramPacket(location, lines)) {
                filter(it)
            }
    }
}
