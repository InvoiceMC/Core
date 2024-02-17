package me.outspending.core

import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.plugin.java.JavaPlugin

object CoreHandler {
    lateinit var core: JavaPlugin
    lateinit var miniMessage: MiniMessage

    fun setup(core: JavaPlugin) {
        setCore(core)
        setMiniMessage(MiniMessage.miniMessage())
    }

    private fun setCore(core: JavaPlugin): CoreHandler {
        require(!this::core.isInitialized) { "Core already set" }

        this.core = core
        return this
    }

    private fun setMiniMessage(miniMessage: MiniMessage): CoreHandler {
        require(!this::miniMessage.isInitialized) { "MiniMessage already set" }

        this.miniMessage = miniMessage
        return this
    }
}