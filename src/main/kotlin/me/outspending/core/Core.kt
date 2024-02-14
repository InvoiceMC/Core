package me.outspending.core

import com.azuyamat.maestro.bukkit.Maestro
import com.azuyamat.maestro.bukkit.data.CommandData
import me.outspending.core.config.impl.MessagesConfig
import me.outspending.core.config.impl.QuestsConfig
import me.outspending.core.gameplay.crates.CratesManager
import me.outspending.core.listeners.ListenerRegistry
import me.outspending.core.misc.broadcaster.BroadcastHandler
import me.outspending.core.misc.broadcaster.BroadcastManager
import me.outspending.core.misc.scoreboard.ScoreboardHandler
import me.outspending.core.storage.DatabaseManager
import net.luckperms.api.LuckPerms
import org.bukkit.plugin.RegisteredServiceProvider
import org.bukkit.plugin.java.JavaPlugin
import kotlin.time.measureTime

const val COMMANDS_PACKAGE = "me.outspending.core.commands"

lateinit var core: Core

class Core : JavaPlugin() {
    var commandsList: MutableList<CommandData> = mutableListOf()
    val messageConfig = MessagesConfig(this)
    val questsConfig = QuestsConfig(this)
    val broadcastManager = BroadcastManager()
    lateinit var  cratesManager: CratesManager

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
            scoreboardHandler = ScoreboardHandler()

            cratesManager = CratesManager()
            ListenerRegistry.registerEvents(server.pluginManager)
            BroadcastHandler.registerAllBroadcasts()
            DatabaseManager.setupDatabase()
        }

        logger.info("Core has finished loading in $time!")
    }

    override fun onDisable() {
        DatabaseManager.stopDatabase()
    }

    private fun setupLuckPerms() {
        val service: RegisteredServiceProvider<LuckPerms>? =
            server.servicesManager.getRegistration(LuckPerms::class.java)
        service?.let { luckPermsProvider = it.provider }
    }
}
