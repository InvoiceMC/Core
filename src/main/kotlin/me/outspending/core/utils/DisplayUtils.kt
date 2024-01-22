package me.outspending.core.utils

import me.outspending.core.utils.Utilities.toComponent
import net.kyori.adventure.text.Component
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.BlockDisplay
import org.bukkit.entity.Display.Billboard
import org.bukkit.entity.EntityType
import org.bukkit.entity.TextDisplay

object DisplayUtils {
    @JvmStatic fun blockDisplayBuilder() = BlockDisplayBuilder()

    @JvmStatic fun textDisplayBuilder() = TextDisplayBuilder()

    @JvmStatic
    fun spawnBlockDisplay(location: Location, type: Material): BlockDisplay {
        val world = location.world

        val blockDisplay: BlockDisplay =
            world.spawnEntity(location, EntityType.BLOCK_DISPLAY) as BlockDisplay
        blockDisplay.block = type.createBlockData()

        return blockDisplay
    }

    @JvmStatic
    fun spawnBlockDisplay(
        location: Location,
        type: Material,
        customName: Component?
    ): BlockDisplay = spawnBlockDisplay(location, type, customName, Billboard.CENTER)

    @JvmStatic
    fun spawnBlockDisplay(
        location: Location,
        type: Material,
        customName: Component?,
        billboard: Billboard,
    ): BlockDisplay = spawnBlockDisplay(location, type, customName, billboard, 1f, 1f)

    @JvmStatic
    fun spawnBlockDisplay(
        location: Location,
        type: Material,
        customName: Component?,
        billboard: Billboard,
        displayHeight: Float,
        displayWidth: Float,
    ): BlockDisplay {
        val blockDisplay: BlockDisplay = spawnBlockDisplay(location, type)

        blockDisplay.apply {
            this.billboard = billboard
            this.displayHeight = displayHeight
            this.displayWidth = displayWidth

            customName?.let {
                this.isCustomNameVisible = true
                customName(it)
            }
        }

        return blockDisplay
    }

    @JvmStatic
    fun spawnTextDisplay(location: Location, lines: Array<String>): TextDisplay =
        spawnTextDisplay(location, lines, Billboard.CENTER)

    @JvmStatic
    fun spawnTextDisplay(location: Location, lines: List<String>): TextDisplay =
        spawnTextDisplay(location, lines, Billboard.CENTER)

    @JvmStatic
    fun spawnTextDisplay(
        location: Location,
        lines: Array<String>,
        billboard: Billboard,
    ): TextDisplay = spawnTextDisplay(location, lines.toList(), billboard)

    @JvmStatic
    fun spawnTextDisplay(
        location: Location,
        lines: List<String>,
        billboard: Billboard
    ): TextDisplay {
        val world = location.world

        val textDisplay: TextDisplay =
            world.spawnEntity(location, EntityType.TEXT_DISPLAY) as TextDisplay
        textDisplay.apply {
            this.billboard = billboard
            this.text(lines.joinToString("\n").toComponent())
        }

        return textDisplay
    }

    class BlockDisplayBuilder {
        private var location: Location? = null
        private var type: Material? = null
        private var customName: Component? = null
        private var billboard: Billboard = Billboard.CENTER
        private var displayHeight: Float = 1f
        private var displayWidth: Float = 1f

        fun location(location: Location) = apply { this.location = location }

        fun type(type: Material) = apply { this.type = type }

        fun customName(customName: Component) = apply { this.customName = customName }

        fun billboard(billboard: Billboard) = apply { this.billboard = billboard }

        fun displayHeight(displayHeight: Float) = apply { this.displayHeight = displayHeight }

        fun displayWidth(displayWidth: Float) = apply { this.displayWidth = displayWidth }

        fun build() {
            requireNotNull(this.location) { "Location cannot be null!" }
            requireNotNull(this.type) { "Type cannot be null!" }

            spawnBlockDisplay(
                location!!,
                type!!,
                customName,
                billboard,
                displayHeight,
                displayWidth
            )
        }
    }

    class TextDisplayBuilder {
        private var location: Location? = null
        private var lines: List<String>? = null
        private var billboard: Billboard = Billboard.CENTER

        fun location(location: Location) = apply { this.location = location }

        fun lines(lines: List<String>) = apply { this.lines = lines }

        fun billboard(billboard: Billboard) = apply { this.billboard = billboard }

        fun build() {
            requireNotNull(this.location) { "Location cannot be null!" }
            requireNotNull(this.lines) { "Lines cannot be null!" }

            spawnTextDisplay(location!!, lines!!, billboard)
        }
    }
}
