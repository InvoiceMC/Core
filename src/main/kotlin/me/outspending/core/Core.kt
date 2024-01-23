package me.outspending.core

import me.outspending.core.broadcaster.BroadcastManager
import me.outspending.core.commands.CommandRegistry
import me.outspending.core.commands.completions.CompletionsRegistry.registerCompletions
import me.outspending.core.enchants.events.ExplosionEvent
import me.outspending.core.leaderboards.LeaderboardManager
import me.outspending.core.listeners.ChatListeners
import me.outspending.core.listeners.CommandListeners
import me.outspending.core.listeners.MiscListeners
import me.outspending.core.listeners.PlayerListeners
import me.outspending.core.mine.MineListener
import me.outspending.core.scoreboard.ScoreboardHandler
import me.outspending.core.storage.DataHandler
import me.outspending.core.storage.PlayerData
import me.outspending.core.storage.database.Database
import me.outspending.core.storage.database.DatabaseHandler
import me.outspending.core.storage.database.PlayerDatabase
import me.sparky983.vision.paper.PaperVision
import net.kyori.adventure.text.minimessage.MiniMessage
import net.luckperms.api.LuckPerms
import org.bukkit.plugin.PluginManager
import org.bukkit.plugin.RegisteredServiceProvider
import org.bukkit.plugin.java.JavaPlugin
import java.util.*
import kotlin.time.measureTime

val instance
    get() = JavaPlugin.getPlugin(Core::class.java)

class Core : JavaPlugin() {
    companion object {
        lateinit var miniMessage: MiniMessage
        lateinit var playerDatabase: Database<UUID, PlayerData>
        lateinit var scoreboardHandler: ScoreboardHandler
        lateinit var broadcastManager: BroadcastManager
        lateinit var leaderboardManager: LeaderboardManager
        lateinit var paperVision: PaperVision
        lateinit var luckPermsProvider: LuckPerms
    }

    override fun onEnable() {
        val time = measureTime {
            // Initialize variables
            miniMessage = MiniMessage.miniMessage()
            playerDatabase = PlayerDatabase()
            scoreboardHandler = ScoreboardHandler()
            broadcastManager = BroadcastManager()
            leaderboardManager = LeaderboardManager()
            paperVision = PaperVision.create(this)

            // LuckPerms
            setupLuckperms()

            // Register Commands
            registerAllCommands()

            // Broadcast Manager
            registerBroadcasts()

            // Check database file
            DatabaseHandler.setupDatabase()
            DataHandler.startup()
            playerDatabase.createTable()

            // Register Events
            registerEvents(server.pluginManager)

            // Register completions
            registerCompletions()
        }

        logger.info("Finished loading Core in $time!")
    }

    override fun onDisable() {
        playerDatabase.updateAllData()

        DatabaseHandler.closeConnection()
    }

    private fun setupLuckperms() {
        val service: RegisteredServiceProvider<LuckPerms>? =
            server.servicesManager.getRegistration(LuckPerms::class.java)
        service?.let { luckPermsProvider = it.provider }
    }

    private fun registerBroadcasts() {
        logger.info("Registering broadcasts...")
        broadcastManager.addBroadcast("Welcome", "Epicness")
        broadcastManager.addBroadcast("Welcome1", "Epicness1")
        broadcastManager.addBroadcast("Welcome2", "Epicness2")
        broadcastManager.addBroadcast("Welcome3", "Epicness3")

        broadcastManager.start()
        logger.info("Finished registering broadcasts!")
    }

    private fun registerEvents(pluginManager: PluginManager) {
        pluginManager.registerEvents(ChatListeners(), this)
        pluginManager.registerEvents(CommandListeners(), this)
        pluginManager.registerEvents(MiscListeners(), this)
        pluginManager.registerEvents(PlayerListeners(), this)
        pluginManager.registerEvents(MineListener(), this)
        pluginManager.registerEvents(ExplosionEvent(), this)
    }

    private fun registerAllCommands() {
        logger.info("Registering commands...")
        CommandRegistry().registerCommands()
        logger.info("Finished registering commands!")
    }
}
