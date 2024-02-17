package me.outspending.core

import me.outspending.core.commands.CommandRegistry
import me.outspending.core.scoreboard.ScoreboardHandler
import org.bukkit.plugin.java.JavaPlugin
import kotlin.time.measureTime

lateinit var core: Core

class Core : JavaPlugin() {

    override fun onEnable() {
        val time = measureTime {
            CoreHandler.setup(this)
            CommandRegistry.registerAll()
            
//
//            cratesManager = CratesManager()
//            ListenerRegistry.registerEvents(server.pluginManager)
//            BroadcastHandler.registerAllBroadcasts()
//            DatabaseManager.setupDatabase()
//
//            DiscordBot.start()
        }

        logger.info("Core has finished loading in $time!")
    }

//    override fun onDisable() {
//        DatabaseManager.stopDatabase()
//    }

//    private fun setupLuckPerms() {
//        val service: RegisteredServiceProvider<LuckPerms>? =
//            server.servicesManager.getRegistration(LuckPerms::class.java)
//        service?.let { luckPermsProvider = it.provider }
//    }
}
