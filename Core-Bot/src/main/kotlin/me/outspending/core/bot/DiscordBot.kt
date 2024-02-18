package me.outspending.core.bot

import me.outspending.core.CoreHandler.core
import me.outspending.core.bot.listeners.discord.ReadyListener
import me.outspending.core.bot.listeners.discord.UserEvents
import me.outspending.core.config.impl.DiscordConfig
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel
import net.dv8tion.jda.api.requests.GatewayIntent

internal val discordConfig = DiscordConfig(core)

object DiscordBot {
    internal val logChannel: TextChannel by lazy { getLogChannel() }
    internal val msgChannel: TextChannel by lazy { getMsgChannel() }

    private val jda: JDA = JDABuilder.createDefault(
        discordConfig.getToken()
    )
        .setActivity(Activity.watching("over the server"))
        .setEnabledIntents(GatewayIntent.getIntents(GatewayIntent.ALL_INTENTS))
        .addEventListeners(ReadyListener(), UserEvents())
        .build()


    fun start() {
        jda.awaitReady()
        updateActivity()
    }

    fun getGuild() = jda.getGuildById(discordConfig.getGuildId()) ?: throw IllegalStateException("Guild not found")
    fun getLogChannel() = getGuild().getTextChannelById(discordConfig.getLogChannelId()) ?: throw IllegalStateException(
        "Log channel not found"
    )
    fun getMsgChannel() = getGuild().getTextChannelById(discordConfig.getMsgChannelId()) ?: throw IllegalStateException(
        "Message channel not found"
    )

    fun updateActivity() {
        val playersOnline = core.server.onlinePlayers.size
        val maxPlayers = core.server.maxPlayers
        val activity = if (playersOnline == 0) "over the server" else "$playersOnline/$maxPlayers players"
        jda.presence.activity = Activity.watching(activity)
    }
}