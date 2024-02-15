package me.outspending.core.bot.listeners.minecraft

import me.outspending.core.bot.DiscordBot
import me.outspending.core.bot.factories.EmbedFactory
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.server.PluginDisableEvent

class ServerStopListener: Listener {
    @EventHandler
    fun onServerStop(event: PluginDisableEvent) {
        val embed = EmbedFactory("❌ InvoiceMC is shutting down.")
            .setColor("#FFAAAA")
            .setDescription("> Insightful information:")
            .addField("Reason", "Server is shutting down.", true)
            .build()
        DiscordBot.getLogChannel().sendMessageEmbeds(embed).queue()
    }
}