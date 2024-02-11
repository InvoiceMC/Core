package me.outspending.core.listeners

import io.papermc.paper.event.player.AsyncChatEvent
import me.outspending.core.Utilities.format
import me.outspending.core.Utilities.getData
import me.outspending.core.Utilities.getLuckPermsName
import me.outspending.core.Utilities.toComponent
import me.outspending.core.Utilities.toTinyNumber
import me.outspending.core.misc.chat.ChatHandler
import me.outspending.core.storage.data.PlayerData
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

            val playerData: PlayerData = player.getData() ?: PlayerData()
            val hoverText: String = createHoverText(playerData, player.level)

            "<hover:show_text:'$hoverText'>${player.getLuckPermsName()}<gold><bold>${player.level.toTinyNumber()}</bold></hover> <gray>»<white> <message>"
                .toComponent(msg)
        }
    }
}
