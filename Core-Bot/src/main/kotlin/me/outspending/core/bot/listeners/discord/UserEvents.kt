package me.outspending.core.bot.listeners.discord

import me.outspending.core.CoreHandler.core
import me.outspending.core.bot.DiscordBot
import me.outspending.core.bot.discordBot
import me.outspending.core.helpers.FormatHelper.Companion.parse
import net.dv8tion.jda.api.entities.emoji.Emoji
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class UserEvents : ListenerAdapter() {
    @Override
    override fun onMessageReceived(event: MessageReceivedEvent) {
        if (event.author.isBot) return
        if (discordBot.getLogChannel() != event.channel) return

        val author = event.author.name
        val message = event.message.contentRaw

        core.server.broadcast("<main>[DISCORD] $author<gray>: $message".parse(true))

        // React to the message
        event.message.addReaction(Emoji.fromUnicode("✅")).queue()
    }
}