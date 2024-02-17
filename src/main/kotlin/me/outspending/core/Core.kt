package me.outspending.core

import me.outspending.core.bot.DiscordBot
import me.outspending.core.commands.CommandRegistry
import me.outspending.core.data.DatabaseManager
import me.outspending.core.listeners.ListenerRegistry
import me.outspending.core.misc.broadcaster.BroadcastHandler
import org.bukkit.plugin.java.JavaPlugin
import kotlin.time.measureTime

lateinit var core: Core

class Core : JavaPlugin() {

    override fun onEnable() {
        val time = measureTime {
            CoreHandler.setup(this)
            DiscordBot.start()

            ListenerRegistry.registerEvents()
            CommandRegistry.registerAll()

            DatabaseManager.setupDatabase()
            // BroadcastHandler.registerAllBroadcasts()
        }

        logger.info("Core has finished loading in $time!")
    }

    override fun onDisable() {
        DatabaseManager.stopDatabase()
    }

//    private fun setupLuckPerms() {
//        val service: RegisteredServiceProvider<LuckPerms>? =
//            server.servicesManager.getRegistration(LuckPerms::class.java)
//        service?.let { luckPermsProvider = it.provider }
//    }
}
