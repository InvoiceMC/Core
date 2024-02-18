package me.outspending.core.bot.listeners.minecraft

import io.papermc.paper.event.player.AsyncChatEvent
import me.outspending.core.CoreHandler.core
import me.outspending.core.bot.DiscordBot
import me.outspending.core.bot.factories.EmbedFactory
import net.kyori.adventure.text.TextComponent
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerCommandPreprocessEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

class PlayerEvents : Listener {

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val embed = EmbedFactory("👋 Player Joined")
            .setDescription("Welcome to the server, ${event.player.name}!")
            .setColor("#AAAAFF")
            .setThumbnail(getImage(event.player))
            .setFooter(playerCount())
            .build()
        DiscordBot.logChannel.sendMessageEmbeds(embed).queue()
        DiscordBot.updateActivity()
    }

    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent) {
        val embed = EmbedFactory("😒 Player Left")
            .setDescription("Goodbye, ${event.player.name}!")
            .setColor("#FFAAAA")
            .setThumbnail(getImage(event.player))
            .setFooter(playerCount())
            .build()
        DiscordBot.logChannel.sendMessageEmbeds(embed).queue()
        DiscordBot.updateActivity()
    }

    @EventHandler
    fun onChat(event: AsyncChatEvent) {
        val player = event.player
        val message = (event.message() as TextComponent).content()

        val embed = EmbedFactory("🗨️ Chat")
            .setDescription("**${player.name}**: $message")
            .setColor("#FFFFFF")

        if (event.isCancelled)
            embed.setColor("#FF0000")

        DiscordBot.logChannel.sendMessageEmbeds(embed.build()).queue()
    }

    @EventHandler(priority = EventPriority.NORMAL)
    fun onCommand(event: PlayerCommandPreprocessEvent) {
        if (event.isCancelled) return // Should be cancelled if the command is blacklisted in ChatListeners.kt in Core-Listeners

        val player = event.player
        val command = event.message

        val embed = EmbedFactory("⌨️ Command")
            .setDescription("**${player.name}**: `$command`")
            .setColor("#222222")

        DiscordBot.msgChannel.sendMessageEmbeds(embed.build()).queue()
    }

    private fun getImage(player: Player) = "https://crafatar.com/avatars/${player.uniqueId}?size=128&overlay"
    private fun playerCount() = "Players: ${core.server.onlinePlayers.size}/${core.server.maxPlayers}"
}