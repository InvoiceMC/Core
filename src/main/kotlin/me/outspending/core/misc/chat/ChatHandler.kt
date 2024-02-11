package me.outspending.core.misc.chat

import me.outspending.core.Utilities.toComponent
import me.outspending.core.misc.helpers.enums.CustomSound
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.JoinConfiguration
import net.kyori.adventure.text.TextReplacementConfig
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import org.bukkit.Bukkit
import org.bukkit.inventory.ItemStack

const val ITEM_FORMAT: String = "<dark_gray>[<hover:show_text:'<lore>'><displayname></hover> <gray><amount><dark_gray>]"

object ChatHandler {
    private val plainSerializer = PlainTextComponentSerializer.builder().build()

    private fun convertItemToComponent(item: ItemStack): Component {
        val displayName: Component = item.itemMeta?.displayName()!!
        val lore: List<Component> = item.itemMeta?.lore() ?: emptyList()
        val amount: Int = item.amount

        val displayNamePlaceholder = Placeholder.component("displayname", displayName)
        val amountPlaceholder = Placeholder.component("amount", "x$amount".toComponent())
        val lorePlaceholder =
            Placeholder.component(
                "lore",
                Component.join(JoinConfiguration.separator(Component.newline()), lore)
            )

        return ITEM_FORMAT.toComponent(lorePlaceholder, displayNamePlaceholder, amountPlaceholder)
    }

    fun builder(component: Component): Builder = Builder(component)

    fun replaceItem(component: Component, item: ItemStack): Component {
        if (item.type.isAir) return component
        val replacement = TextReplacementConfig.builder()
            .matchLiteral("[item]").replacement(convertItemToComponent(item)).build()

        return component.replaceText(replacement)
    }

    fun pingPlayers(message: Component): Component {
        val plainMessage = plainSerializer.serialize(message)

        var newComponent = message
        Bukkit.getOnlinePlayers()
            .filter { plainMessage.contains(it.name) }
            .forEach {
                val replacement = TextReplacementConfig.builder()
                    .matchLiteral(it.name)
                    .replacement("<yellow><u>@${it.name}".toComponent())
                    .build()

                newComponent = message.replaceText(replacement)
                CustomSound.Ping().playSound(it)
            }

        return newComponent
    }

    class Builder(private val component: Component) {
        private var message: Component = component

        fun replaceItem(item: ItemStack): Builder {
            message = ChatHandler.replaceItem(message, item)
            return this
        }

        fun pingPlayers(): Builder {
            message = ChatHandler.pingPlayers(message)
            return this
        }

        fun build(): Component = message

    }
}