package me.outspending.core

import com.azuyamat.maestro.bukkit.Maestro
import com.azuyamat.maestro.bukkit.data.CommandData
import me.outspending.core.broadcaster.BroadcastManager
import me.outspending.core.config.ConfigManager
import me.outspending.core.config.impl.MessagesConfig
import me.outspending.core.leaderboards.LeaderboardManager
import me.outspending.core.listeners.ChatListeners
import me.outspending.core.listeners.CommandListeners
import me.outspending.core.listeners.MiscListeners
import me.outspending.core.listeners.PlayerListeners
import me.outspending.core.mine.MineListener
import me.outspending.core.scoreboard.ScoreboardHandler
import me.outspending.core.storage.DataHandler
import me.outspending.core.storage.data.PlayerData
import me.outspending.core.storage.serializers.UUIDSerializer
import me.outspending.munch.Munch
import me.outspending.munch.connection.MunchConnection
import me.outspending.munch.serializer.SerializerFactory
import net.kyori.adventure.text.minimessage.MiniMessage
import net.luckperms.api.LuckPerms
import org.bukkit.plugin.PluginManager
import org.bukkit.plugin.RegisteredServiceProvider
import org.bukkit.plugin.java.JavaPlugin
import java.util.*
import kotlin.time.measureTime

val instance
    get() = JavaPlugin.getPlugin(Core::class.java)
var commands: MutableList<CommandData> = mutableListOf()

// Data Stuff
val database = MunchConnection.create()
val munchPlayerData = Munch.create(PlayerData::class).process<UUID>()

// Config stuff
val messageConfig = MessagesConfig();

class Core : JavaPlugin() {
    companion object {
        lateinit var miniMessage: MiniMessage
        lateinit var scoreboardHandler: ScoreboardHandler
        lateinit var broadcastManager: BroadcastManager
        lateinit var leaderboardManager: LeaderboardManager
        lateinit var luckPermsProvider: LuckPerms
    }

    override fun onEnable() {
        val time = measureTime {
            // Initialize variables
            miniMessage = MiniMessage.miniMessage()
            scoreboardHandler = ScoreboardHandler()
            broadcastManager = BroadcastManager()
            leaderboardManager = LeaderboardManager()

            // Register Commands
            val maestro = Maestro(this).apply {
                registerCommands("me.outspending.core.commands.impl")
            }
            commands = maestro.commands

            // Setup Databases
            setupDatabases()

            // LuckPerms
            setupLuckperms()

            // Broadcast Manager
            registerBroadcasts()

            // Check database file
            DataHandler.startup()

            // Register Events
            registerEvents(server.pluginManager)

            // Setup config
            messageConfig.load()
        }

        logger.info("Finished loading Core in $time!")
    }

    override fun onDisable() {
        val values = DataHandler.playerData.values.toList()
        database.updateAllData(munchPlayerData, values)

        database.disconnect()
    }

    private fun setupLuckperms() {
        val service: RegisteredServiceProvider<LuckPerms>? =
            server.servicesManager.getRegistration(LuckPerms::class.java)
        service?.let { luckPermsProvider = it.provider }
    }

    private fun setupDatabases() {
        val folder = dataFolder
        if (!folder.exists()) folder.mkdirs()

        database.connect(folder, "database.db")
        database.createTable(munchPlayerData)

        SerializerFactory.registerSerializer(UUIDSerializer())
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
    }
}
