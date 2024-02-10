package me.outspending.core.listeners

import io.papermc.paper.event.player.AsyncChatEvent
import me.outspending.core.Utilities.format
import me.outspending.core.Utilities.getData
import me.outspending.core.Utilities.toComponent
import me.outspending.core.Utilities.toTinyNumber
import me.outspending.core.core
import me.outspending.core.storage.data.PlayerData
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.JoinConfiguration
import net.kyori.adventure.text.TextReplacementConfig
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.inventory.ItemStack

class ChatListeners : Listener {
    private val PLAYER_STATS_HOVER_MESSAGE =
        listOf(
                "",
                "<green>\$%s <dark_green>ᴍᴏɴᴇʏ",
                "<yellow>⛁%s <gold>ɢᴏʟᴅ",
                "<gray>%s <dark_gray>ʙʀᴏᴋᴇɴ",
                "<#e8d47d>%s <#debe33>ʟᴇᴠᴇʟ",
                "<#d283de>%s <#b36de8>ᴘʀᴇꜱᴛɪɢᴇ",
                ""
            )
            .joinToString("\n")

    private fun convertItem(item: ItemStack): Component {
        val displayName: Component = item.itemMeta?.displayName()!!
        val lore: List<Component> = item.itemMeta?.lore() ?: emptyList()
        val amount: Int = item.amount

        val displayNamePlaceholder = Placeholder.component("displayname", displayName)
        val lorePlaceholder =
            Placeholder.component(
                "lore",
                Component.join(JoinConfiguration.separator(Component.newline()), lore)
            )

        return "<dark_gray>[<hover:show_text:'<lore>'><displayname></hover> <gray>x$amount<dark_gray>]"
            .toComponent(lorePlaceholder, displayNamePlaceholder)
    }

    private fun replace(component: Component, item: ItemStack): Component {
        if (item.type.isAir) return component

        val replacement =
            TextReplacementConfig.builder()
                .matchLiteral("[item]")
                .replacement(convertItem(item))
                .build()

        return component.replaceText(replacement)
    }

    @EventHandler
    fun onChat(e: AsyncChatEvent) {
        e.renderer { player, sourceDisplayName, message, _ ->
            val displayName = Placeholder.component("displayname", sourceDisplayName)
            val msg = Placeholder.component("message", message)

            val playerData: PlayerData = player.getData() ?: PlayerData()
            val playersPrefix: String =
                core.luckPermsProvider.userManager
                    .getUser(player.uniqueId)
                    ?.cachedData
                    ?.metaData
                    ?.prefix ?: "<gray>"

            val hoverText: String =
                PLAYER_STATS_HOVER_MESSAGE.format(
                    playerData.balance.format(),
                    playerData.gold.format(),
                    playerData.blocksBroken.format(),
                    player.level,
                    playerData.prestige,
                )

            val newMessage =
                "<hover:show_text:'$hoverText'>$playersPrefix<displayname><gold><bold>${player.level.toTinyNumber()}</bold></hover> <gray>»<white> <message>"
                    .toComponent(displayName, msg)

            replace(newMessage, player.inventory.itemInMainHand)
        }
    }
}
