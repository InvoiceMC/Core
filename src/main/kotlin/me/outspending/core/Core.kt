package me.outspending.core

import com.azuyamat.maestro.bukkit.Maestro
import com.azuyamat.maestro.bukkit.data.CommandData
import me.outspending.core.config.impl.MessagesConfig
import me.outspending.core.listeners.ChatListeners
import me.outspending.core.listeners.CommandListeners
import me.outspending.core.listeners.MiscListeners
import me.outspending.core.listeners.PlayerListeners
import me.outspending.core.mining.pickaxe.PickaxeListener
import me.outspending.core.misc.broadcaster.BroadcastHandler
import me.outspending.core.misc.broadcaster.BroadcastManager
import me.outspending.core.misc.leaderboard.LeaderboardManager
import me.outspending.core.misc.scoreboard.ScoreboardHandler
import me.outspending.core.storage.DatabaseHandler
import net.luckperms.api.LuckPerms
import org.bukkit.plugin.PluginManager
import org.bukkit.plugin.RegisteredServiceProvider
import org.bukkit.plugin.java.JavaPlugin
import kotlin.time.measureTime

const val COMMANDS_PACKAGE = "me.outspending.core.commands.impl"

lateinit var core: Core

class Core : JavaPlugin() {
    var commandsList: MutableList<CommandData> = mutableListOf()
    val messageConfig = MessagesConfig(this)
    val broadcastManager = BroadcastManager()

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

            setupLuckPerms()

            messageConfig.load()
            scoreboardHandler = ScoreboardHandler()

            registerEvents(server.pluginManager)

            BroadcastHandler.registerAllBroadcasts()
            DatabaseHandler.setupDatabase()
        }

        logger.info("Finished loading Core in $time!")
    }

    override fun onDisable() {
        DatabaseHandler.stopDatabase()
    }

    private fun setupLuckPerms() {
        val service: RegisteredServiceProvider<LuckPerms>? =
            server.servicesManager.getRegistration(LuckPerms::class.java)
        service?.let { luckPermsProvider = it.provider }
    }

    private fun registerEvents(pluginManager: PluginManager) {
        listOf(
                ChatListeners(),
                CommandListeners(),
                MiscListeners(),
                PlayerListeners(),
                PickaxeListener()
            )
            .map { pluginManager.registerEvents(it, this) }
    }
}
