package me.outspending.core.bot.listeners.discord

import me.outspending.core.bot.DiscordBot
import me.outspending.core.bot.factories.EmbedFactory
import me.outspending.core.core
import net.dv8tion.jda.api.events.session.ReadyEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class ReadyListener: ListenerAdapter() {
    @Override
    override fun onReady(event: ReadyEvent) {
        val commandsCount = core.commandsList.size
        val embed = EmbedFactory("✅ InvoiceMC is ready.")
            .setColor("#AAFFAA")
            .setDescription("> Insightful information:")
            .addField("Commands", "`$commandsCount`", true)
            .build()
        DiscordBot.getLogChannel().sendMessageEmbeds(embed).queue()
    }
}