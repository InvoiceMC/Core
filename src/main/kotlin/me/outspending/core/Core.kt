package me.outspending.core

import com.azuyamat.maestro.bukkit.Maestro
import com.azuyamat.maestro.bukkit.data.CommandData
import me.outspending.core.broadcaster.BroadcastManager
import me.outspending.core.config.impl.BroadcastsConfig
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
import me.outspending.core.storage.serializers.BoundingBoxSerializer
import me.outspending.core.storage.serializers.LocationSerializer
import me.outspending.core.storage.serializers.UUIDSerializer
import me.outspending.munch.Munch
import me.outspending.munch.connection.MunchConnection
import me.outspending.munch.serializer.SerializerFactory
import net.luckperms.api.LuckPerms
import org.bukkit.plugin.PluginManager
import org.bukkit.plugin.RegisteredServiceProvider
import org.bukkit.plugin.java.JavaPlugin
import java.util.*
import kotlin.time.measureTime

const val COMMANDS_PACKAGE = "me.outspending.core.commands.impl"
const val DATABASE_NAME = "database.db"

lateinit var core: Core

class Core : JavaPlugin() {
    var commandsList: MutableList<CommandData> = mutableListOf()
    val database = MunchConnection.create()
    val munchPlayerData = Munch.create(PlayerData::class).process<UUID>()
    val messageConfig = MessagesConfig(this)
    val broadcastManager = BroadcastManager()
    val broadcastsConfig = BroadcastsConfig(this)

    private val leaderboardManager = LeaderboardManager() // Make public once it is used

    lateinit var scoreboardHandler: ScoreboardHandler
    lateinit var luckPermsProvider: LuckPerms

    override fun onEnable() {
        core = this
        val time = measureTime {
            // Register Commands
            Maestro(this).apply {
                registerCommands(COMMANDS_PACKAGE)
                commandsList = this.commands
            }

            messageConfig.load()
            broadcastsConfig
                .load() // It is important to load the broadcasts config before registering
                        // broadcasts

            scoreboardHandler = ScoreboardHandler()

            setupDatabases()
            setupLuckPerms()
            registerBroadcasts()
            registerEvents(server.pluginManager)

            DataHandler.startup()
        }

        logger.info("Finished loading Core in $time!")
    }

    override fun onDisable() {
        val values = DataHandler.playerData.values.toList()
        database.updateAllData(munchPlayerData, values)

        database.disconnect()
    }

    private fun setupLuckPerms() {
        val service: RegisteredServiceProvider<LuckPerms>? =
            server.servicesManager.getRegistration(LuckPerms::class.java)
        service?.let { luckPermsProvider = it.provider }
    }

    private fun setupDatabases() {
        val folder = dataFolder
        if (!folder.exists()) folder.mkdirs()

        database.connect(folder, DATABASE_NAME)
        database.createTable(munchPlayerData)

        SerializerFactory.registerSerializers(
            UUIDSerializer(),
            LocationSerializer(),
            BoundingBoxSerializer()
        )
    }

    private fun registerBroadcasts() {
        logger.info("Registering broadcasts...")
        broadcastManager.addBroadcast("Welcome", "Epicness")
        broadcastManager.addBroadcast("Welcome1", "Epicness1")
        broadcastManager.addBroadcast("Welcome2", "Epicness2")
        broadcastManager.addBroadcast("Welcome3", "Epicness3")
        broadcastManager.registerFromConfig(broadcastsConfig)

        broadcastManager.start()
        logger.info("Finished registering broadcasts!")
    }

    private fun registerEvents(pluginManager: PluginManager) {
        listOf(
                ChatListeners(),
                CommandListeners(),
                MiscListeners(),
                PlayerListeners(),
                MineListener()
            )
            .map { pluginManager.registerEvents(it, this) }
    }
}
