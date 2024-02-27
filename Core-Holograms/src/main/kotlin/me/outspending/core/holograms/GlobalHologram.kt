package me.outspending.core.holograms

import me.outspending.core.helpers.FormatHelper.Companion.parse
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.JoinConfiguration
import org.bukkit.Location
import org.bukkit.entity.Display.Billboard
import org.bukkit.entity.EntityType
import org.bukkit.entity.TextDisplay

class GlobalHologram(
    private var lines: MutableList<Component>,
    spawnLocation: Location,
    private val hologramBillboard: Billboard,
    private val hasBackground: Boolean = false
) : Hologram {
    constructor(lines: Collection<Component>, spawnLocation: Location, hologramBillboard: Billboard) :
            this(lines.toMutableList(), spawnLocation, hologramBillboard)

    private val textDisplay: TextDisplay =
        spawnLocation.world.spawnEntity(spawnLocation, EntityType.TEXT_DISPLAY) as TextDisplay

    init {
        textDisplay.apply {
            this.text(Component.join(JoinConfiguration.newlines(), lines))
            this.billboard = hologramBillboard
            this.isDefaultBackground = hasBackground
        }
    }

    override fun teleport(location: Location) {
        textDisplay.teleport(location)
    }

    override fun updateLines() {
        textDisplay.text(Component.join(JoinConfiguration.newlines(), lines))
    }

    override fun setLines(lines: List<Component>) {
        this.lines = lines.toMutableList()
        updateLines()
    }

    override fun addLine(line: Component) {
        textDisplay.text()
            .append(Component.newline())
            .append(line)
    }

    override fun updateLine(index: Int, line: Component) {
        lines[index] = line
        updateLines()
    }

    override fun removeLine(index: Int) {
        lines.removeAt(index)
        updateLines()
    }

    override fun kill() {
        textDisplay.remove()
    }
}