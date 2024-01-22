package me.outspending.core.utils.types

import me.outspending.core.utils.Utilities.toComponent
import net.kyori.adventure.title.Title
import net.kyori.adventure.title.Title.Times
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.time.Duration

class CTitle(
    title: String,
    subtitle: String,
    fadeIn: Duration = Duration.ofMillis(500),
    stay: Duration = Duration.ofMillis(1000),
    fadeOut: Duration = Duration.ofMillis(500),
) : CustomType {
    private val componentTitle: Title =
        Title.title(
            title.toComponent(),
            subtitle.toComponent(),
            Times.times(
                fadeIn,
                stay,
                fadeOut,
            ),
        )

    override fun send(player: Player) = player.showTitle(componentTitle)

    override fun send(players: Collection<Player>) = players.forEach { send(it) }

    override fun broadcast() = send(Bukkit.getOnlinePlayers())

    override fun broadcast(filter: (Player) -> Boolean) =
        send(Bukkit.getOnlinePlayers().filter(filter))
}
