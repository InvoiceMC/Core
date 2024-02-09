package me.outspending.core.listeners

import io.papermc.paper.event.player.AsyncChatEvent
import me.outspending.core.Utilities.format
import me.outspending.core.Utilities.getData
import me.outspending.core.Utilities.toComponent
import me.outspending.core.Utilities.toTinyNumber
import me.outspending.core.core
import me.outspending.core.storage.data.PlayerData
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

    @EventHandler
    fun onChat(e: AsyncChatEvent) {
        e.renderer { player, sourceDisplayName, message, _ ->
            val displayName = Placeholder.component("displayname", sourceDisplayName)
            val msg = Placeholder.component("message", message)
            val playerData: PlayerData = player.getData() ?: PlayerData()
            val playersPrefix: String = core.luckPermsProvider.userManager.getUser(player.uniqueId)?.cachedData?.metaData?.prefix ?: "<gray>"

            val hoverText: String =
                PLAYER_STATS_HOVER_MESSAGE.format(
                    playerData.balance.format(),
                    playerData.gold.format(),
                    playerData.blocksBroken.format(),
                    player.level,
                    playerData.prestige,
                )

            "<hover:show_text:'$hoverText'>$playersPrefix<displayname><gold><bold>${player.level.toTinyNumber()}</bold></hover> <gray>»<white> <message>"
                .toComponent(displayName, msg)
        }
    }
}