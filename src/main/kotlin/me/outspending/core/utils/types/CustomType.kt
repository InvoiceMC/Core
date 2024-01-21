package me.outspending.core.utils.types

import org.bukkit.entity.Player

interface CustomType {
    fun send(player: Player)

    fun send(players: Collection<Player>)

    fun broadcast()

    fun broadcast(filter: (Player) -> Boolean)
}
