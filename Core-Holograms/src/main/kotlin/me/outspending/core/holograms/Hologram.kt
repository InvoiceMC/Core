package me.outspending.core.holograms

import net.kyori.adventure.text.Component
import org.bukkit.Location

interface Hologram {

    fun teleport(location: Location)

    fun setLines(lines: List<Component>)
    fun addLine(line: Component)

    fun kill()
}