package me.outspending.core.bot.listeners.discord

import me.outspending.core.bot.DiscordBot
import me.outspending.core.bot.discordBot
import me.outspending.core.bot.factories.EmbedFactory
import net.dv8tion.jda.api.events.session.ReadyEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class ReadyListener : ListenerAdapter() {
    @Override
    override fun onReady(event: ReadyEvent) {
        val embed = EmbedFactory("✅ InvoiceMC is ready.")
            .setColor("#AAFFAA")
            .setDescription(
                "Connected to Discord as ${event.jda.selfUser.name}.\n" +
                        "Connected to ${event.jda.guilds.size} guild(s).\n" +
                        "Helping ${event.jda.users.size} users.\n"
            )
            .build()

        discordBot.logChannel.sendMessageEmbeds(embed).queue()
    }
}