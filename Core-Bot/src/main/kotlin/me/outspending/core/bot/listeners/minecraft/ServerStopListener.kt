package me.outspending.core.bot.listeners.minecraft

import me.outspending.core.bot.DiscordBot
import me.outspending.core.bot.factories.EmbedFactory
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.server.PluginDisableEvent
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class ServerStopListener: Listener {
    private val startTime = System.currentTimeMillis()
    @EventHandler
    fun onServerStop(event: PluginDisableEvent) {
        val uptime = System.currentTimeMillis() - startTime
        val duration = uptime.toDuration(DurationUnit.MILLISECONDS)
        val embed = EmbedFactory("❌ InvoiceMC is shutting down.") // Embed doesn't need to be lazy, as it's only used once.
            .setColor("#FFAAAA")
            .setDescription("What a shame. I was having fun.")
            .addField("Uptime", duration.toString(), false)
            .build()
        DiscordBot.getLogChannel().sendMessageEmbeds(embed).queue()
    }
}