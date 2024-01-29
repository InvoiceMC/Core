package me.outspending.core.utils.helpers

import me.outspending.core.instance
import me.outspending.core.utils.helpers.FormatHelper.Companion.parse
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.tag.Tag
import net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import org.bukkit.Bukkit
import java.util.*

val miniMessage = MiniMessage.miniMessage()
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
        val message = miniMessage.deserialize(text, chatcolorResolver(), mainColorResolver())
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
                val player = instance.server.getPlayer(uuid) ?: throw IllegalArgumentException("Player $uuidString not found")

                val color = NamedTextColor.GRAY // TODO: Get the player's chatcolor for real
                Tag.styling(TextColor.color(color.red(), color.green(), color.blue()))
            }
        }

        // <main>
        fun mainColorResolver(): TagResolver {
            return TagResolver.resolver(
                "main"
            ) { args: ArgumentQueue, _ ->
                Tag.styling(TextColor.color(140, 140, 255)) // #8c8cff
            }
        }
    }
}