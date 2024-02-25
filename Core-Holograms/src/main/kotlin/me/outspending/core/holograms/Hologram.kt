package me.outspending.core.holograms

import net.kyori.adventure.text.Component
import org.bukkit.Location

interface Hologram {

    fun teleport(location: Location)

    fun updateLines()
    fun setLines(lines: List<Component>)
    fun addLine(line: Component)
    fun updateLine(index: Int, line: Component)
    fun removeLine(index: Int)

    fun kill()
}