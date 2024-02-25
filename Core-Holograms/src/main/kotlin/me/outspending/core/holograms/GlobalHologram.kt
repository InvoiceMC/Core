package me.outspending.core.holograms

import me.outspending.core.helpers.FormatHelper.Companion.parse
import net.kyori.adventure.text.Component
import org.bukkit.Location
import org.bukkit.entity.Display.Billboard
import org.bukkit.entity.EntityType
import org.bukkit.entity.TextDisplay

class GlobalHologram(
    private val lines: Collection<Component>,
    spawnLocation: Location,
    private val hologramBillboard: Billboard,
    private val hasBackground: Boolean = false
) : Hologram {
    private val textDisplay: TextDisplay =
        spawnLocation.world.spawnEntity(spawnLocation, EntityType.TEXT_DISPLAY) as TextDisplay

    init {
        textDisplay.apply {
            this.text(lines.joinToString("\n").parse())
            this.billboard = hologramBillboard
            this.isDefaultBackground = hasBackground
        }
    }

    override fun teleport(location: Location) {
        textDisplay.teleport(location)
    }

    override fun setLines(lines: List<Component>) {
        textDisplay.text(lines.joinToString("\n").parse())
    }

    override fun addLine(line: Component) {
        textDisplay.text()
            .append(Component.newline())
            .append(line)
    }

    override fun kill() {
        textDisplay.remove()
    }
}