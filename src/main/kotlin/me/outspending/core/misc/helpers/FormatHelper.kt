package me.outspending.core.misc.helpers

import me.outspending.core.core
import me.outspending.core.misc.helpers.FormatHelper.Companion.chatcolorResolver
import me.outspending.core.misc.helpers.FormatHelper.Companion.mainColorResolver
import me.outspending.core.misc.helpers.FormatHelper.Companion.parse
import me.outspending.core.misc.helpers.FormatHelper.Companion.secondColorResolver
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.tag.Tag
import net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import net.kyori.adventure.text.minimessage.tag.standard.StandardTags
import java.util.*
import kotlin.math.max

private val MAIN_COLOR: TextColor = TextColor.color(140, 140, 255) // #8c8cff
private val SECOND_COLOR: (Float) -> TextColor = {
    listOf(MAIN_COLOR.red(), MAIN_COLOR.green())
        .map { n -> max(n * it, 255.0f).toInt() }
        .let { n -> TextColor.color(n[0], n[1], 255) }
}

val miniMessage = MiniMessage.builder()
    .tags(TagResolver.builder()
        .resolver(StandardTags.defaults())
        .resolver(mainColorResolver())
        .resolver(secondColorResolver())
        .resolver(chatcolorResolver())
        .build())
    .build()

val small_caps = mapOf(
    "a" to "ᴀ",
    "b" to "ʙ",
    "c" to "ᴄ",
    "d" to "ᴅ",
    "e" to "ᴇ",
    "f" to "ꜰ",
    "g" to "ɢ",
    "h" to "ʜ",
    "i" to "ɪ",
    "j" to "ᴊ",
    "k" to "ᴋ",
    "l" to "ʟ",
    "m" to "ᴍ",
    "n" to "ɴ",
    "o" to "ᴏ",
    "p" to "ᴘ",
    "q" to "ǫ",
    "r" to "ʀ",
    "s" to "ꜱ",
    "t" to "ᴛ",
    "u" to "ᴜ",
    "v" to "ᴠ",
    "w" to "ᴡ",
    "x" to "x",
    "y" to "ʏ",
    "z" to "ᴢ"
)
val prefixComponent = "<main><bold>INVOICE<reset> <dark_gray>» <gray>".parse()

class FormatHelper(private val text: String) {

    // Convert text to small letters (small caps)
    fun toSmallCaps() = text.map { small_caps[it.toString().lowercase()] ?: it }.joinToString("")

    // Parse text to MiniMessage component
    fun parse(prefix: Boolean = false): Component {
        val message = miniMessage.deserialize(text)
        return if (prefix) prefixComponent.append(message)
        else message
    }

    // Sanitize the text from <> characters
    fun sanitize() = miniMessage.escapeTags(text)

    // Convert text to title case
    fun toTitleCase() = text.split(" ").joinToString(" ") { it.lowercase().replaceFirstChar { char -> char.uppercase() } }

    companion object {
        fun String.parse(prefix: Boolean = false) = FormatHelper(this).parse(prefix)

        // <chatcolor:UUID>
        fun chatcolorResolver(): TagResolver {
            return TagResolver.resolver(
                "chatcolor"
            ) { args: ArgumentQueue, _ ->
                val uuidString = args.popOr("uuid expected").value()
                val uuid = UUID.fromString(uuidString)
                val player = core.server.getPlayer(uuid) ?: throw IllegalArgumentException("Player $uuidString not found")

                val color = NamedTextColor.GRAY // TODO: Get the player's chatcolor for real
                Tag.styling(TextColor.color(color.red(), color.green(), color.blue()))
            }
        }

        // <main>
        fun mainColorResolver(): TagResolver {
            return TagResolver.resolver(
                "main"
            ) { _: ArgumentQueue, _ ->
                Tag.styling(MAIN_COLOR) // #8c8cff
            }
        }

        fun secondColorResolver(): TagResolver {
            return TagResolver.resolver(
                "second"
            ) { args: ArgumentQueue, _ ->
                val intensity = (args.nextOrNull() ?: "2").toString().toFloat()
                Tag.styling(SECOND_COLOR(intensity))
            }
        }

        private fun ArgumentQueue.nextOrNull() = if (hasNext()) pop().value() else null
    }
}