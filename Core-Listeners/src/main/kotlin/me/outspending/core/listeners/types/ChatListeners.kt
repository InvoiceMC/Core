package me.outspending.core.listeners.types

import io.papermc.paper.event.player.AsyncChatEvent
import me.outspending.core.Utilities.format
import me.outspending.core.Utilities.toComponent
import me.outspending.core.Utilities.toTinyNumber
import me.outspending.core.chat.ChatHandler
import me.outspending.core.data.Extensions.getData
import me.outspending.core.data.player.PlayerData
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

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

    private fun createHoverText(playerData: PlayerData, playerLevel: Int): String =
        PLAYER_STATS_HOVER_MESSAGE.format(
            playerData.balance.format(),
            playerData.gold.format(),
            playerData.blocksBroken.format(),
            playerLevel,
            playerData.prestige,
        )

    @EventHandler
    fun onChat(e: AsyncChatEvent) {
        e.renderer { player, _, message, _ ->
            val newMessage: Component =
                ChatHandler.builder(message)
                    .pingPlayers(player)
                    //.replaceItem(player.inventory.itemInMainHand)
                    .build()

            val msg = Placeholder.component("message", newMessage)

            val playerData = player.getData()
            val hoverText = playerData.let {
                if (it != null) {
                    createHoverText(it, player.level)
                } else {
                    "<red>No player data"
                }
            }

            "<hover:show_text:'$hoverText'>${player.name}<gold><bold>${player.level.toTinyNumber()}</bold></hover> <gray>»<white> <message>"
                .toComponent(msg)
        }
    }
}
