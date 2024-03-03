package me.outspending.core

import com.github.shynixn.mccoroutine.bukkit.SuspendingJavaPlugin
import com.github.shynixn.mccoroutine.bukkit.launch
import kotlinx.coroutines.coroutineScope
import me.outspending.core.bot.discordBot
import me.outspending.core.crates.CratesHandler
import me.outspending.core.data.DatabaseManager
import me.outspending.core.data.player.PlayerDataManager
import me.outspending.core.data.player.playerDataManager
import me.outspending.core.heads.HeadsHandler
import me.outspending.core.pmines.data.PmineDataManager
import me.outspending.core.pmines.data.pmineDataManager
import me.outspending.core.registry.RegistryType
import org.bukkit.plugin.java.JavaPlugin
import kotlin.time.measureTime

class Core : SuspendingJavaPlugin() {
    override suspend fun onEnableAsync() {
        CoreHandler.setup(this)

        val time = measureTime {
            discordBot.start()

            RegistryType.LISTENERS.register()
            RegistryType.COMMANDS.register()

            DatabaseManager.setupDatabase()
            playerDataManager.load()
            pmineDataManager.load()

            // BroadcastHandler.registerAllBroadcasts() // Had to remove this for now
            HeadsHandler.loadHeads()
            CratesHandler.load()
        }

        logger.info("Core has finished loading in $time!")
    }

    override suspend fun onDisableAsync() {
        pmineDataManager.unload()
        playerDataManager.unload()

        DatabaseManager.stopDatabase()
    }
}
