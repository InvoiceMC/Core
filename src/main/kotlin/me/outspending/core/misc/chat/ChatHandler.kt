package me.outspending.core.misc.chat

import me.outspending.core.Utilities.toComponent
import me.outspending.core.misc.helpers.FormatHelper.Companion.parse
import me.outspending.core.misc.helpers.enums.CustomSound
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.JoinConfiguration
import net.kyori.adventure.text.TextReplacementConfig
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

const val ITEM_FORMAT: String =
    "<dark_gray>[<white><hover:show_text:'<lore>'><displayname></hover> <gray><amount><dark_gray>]"


object ChatHandler {
    private val plainSerializer = PlainTextComponentSerializer.builder().build()

    fun builder(component: Component): Builder = Builder(component)

    private fun convertItemToComponent(item: ItemStack): Component {
        val displayName: Component = item.itemMeta?.displayName() ?: item.type.name.toComponent()
        val lore: List<Component>? = item.itemMeta?.lore()
        val amount: Int = item.amount

        val displayNamePlaceholder = Placeholder.component("displayname", displayName)
        val amountPlaceholder = Placeholder.component("amount", "x$amount".toComponent())

        val lorePlaceholder = lore?.let {
            Placeholder.component(
                "lore",
                Component.join(JoinConfiguration.separator(Component.newline()), lore)
            )
        }

        return if (lorePlaceholder != null) {
            ITEM_FORMAT.toComponent(lorePlaceholder, displayNamePlaceholder, amountPlaceholder)
        } else {
            ITEM_FORMAT.toComponent(TagResolver.empty(), displayNamePlaceholder, amountPlaceholder)
        }
    }

    fun replaceItem(component: Component, item: ItemStack): Component {
        if (item.type.isAir) return component
        val replacement =
            TextReplacementConfig.builder()
                .matchLiteral("[item]")
                .replacement(convertItemToComponent(item))
                .build()

        return component.replaceText(replacement)
    }

    fun pingPlayers(message: Component, sender: Player): Component {
        val plainMessage = plainSerializer.serialize(message)

        var newComponent = message
        Bukkit.getOnlinePlayers()
            .filter { plainMessage.contains(it.name) }
            .forEach {
                val replacement =
                    TextReplacementConfig.builder()
                        .matchLiteral(it.name)
                        .replacement("<yellow><u>@${it.name}".toComponent())
                        .build()

                newComponent = message.replaceText(replacement)

                CustomSound.Ping().playSound(it)
                it.sendActionBar("<main>${sender.name} <white>has pinged you!".parse())
            }

        return newComponent
    }

    class Builder(private var component: Component) {

        fun replaceItem(item: ItemStack): Builder {
            component = replaceItem(component, item)
            return this
        }

        fun pingPlayers(sender: Player): Builder {
            component = pingPlayers(component, sender)
            return this
        }

        fun build(): Component = component
    }
}
