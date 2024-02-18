package me.outspending.core.helpers

import me.outspending.core.CoreHandler.core
import me.outspending.core.helpers.FormatHelper.Companion.chatcolorResolver
import me.outspending.core.helpers.FormatHelper.Companion.mainColorResolver
import me.outspending.core.helpers.FormatHelper.Companion.parse
import me.outspending.core.helpers.FormatHelper.Companion.secondColorResolver
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.tag.Tag
import net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import net.kyori.adventure.text.minimessage.tag.standard.StandardTags
import java.util.*

private val MAIN_COLOR: TextColor = TextColor.color(232, 132, 56) // #e88438
private val SECOND_COLOR: TextColor = TextColor.color(224, 161, 112) // #e0a170

private val miniMessage = MiniMessage.builder()
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
val prefixComponent = "<main><bold>ɪɴᴠᴏɪᴄᴇ<reset> <dark_gray>→ <gray>".parse()

class FormatHelper(private val text: String) {

    // Convert text to small letters (small caps)
    fun toSmallCaps() = text.map { small_caps[it.toString().lowercase()] ?: it }.joinToString("")

    // Parse text to MiniMessage component
    fun parse(prefix: Boolean = false): Component {
        val message = miniMessage.deserialize(text)
            .decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE) // NOTE: This is a workaround for things being italic by default

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
            ) { _: ArgumentQueue, _ ->
                Tag.styling(SECOND_COLOR)
            }
        }

        private fun ArgumentQueue.nextOrNull() = if (hasNext()) pop().value() else null
    }
}