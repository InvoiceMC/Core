package me.outspending.core.utils.types

import me.outspending.core.utils.Utilities.toComponent
import net.kyori.adventure.bossbar.BossBar
import net.kyori.adventure.bossbar.BossBar.*
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.entity.Player

class CBossbar(text: String, progress: Float, color: Color, overlay: Overlay, vararg flags: Flag) :
    CustomType {
    private val bossBar: BossBar =
        BossBar.bossBar(text.toComponent(), progress, color, overlay, flags.toSet())

    fun updateText(text: String) {
        bossBar.name(text.toComponent())
    }

    fun updateText(text: Component) {
        bossBar.name(text)
    }

    fun updateProgress(progress: Float) {
        bossBar.progress(progress / 100) // Range 0 -> 1
    }

    fun updateColor(color: Color) {
        bossBar.color(color)
    }

    fun updateOverlay(overlay: Overlay) {
        bossBar.overlay(overlay)
    }

    fun remove(player: Player) {
        player.hideBossBar(bossBar)
    }

    override fun send(player: Player) = player.showBossBar(bossBar)

    override fun send(players: Collection<Player>) = players.forEach { send(it) }

    override fun broadcast() = send(Bukkit.getOnlinePlayers())

    override fun broadcast(filter: (Player) -> Boolean) =
        send(Bukkit.getOnlinePlayers().filter(filter))
}
