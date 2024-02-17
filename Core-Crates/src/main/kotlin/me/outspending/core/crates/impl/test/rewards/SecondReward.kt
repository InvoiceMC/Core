package me.outspending.core.crates.impl.test.rewards

import me.outspending.core.crates.types.IReward
import org.bukkit.Bukkit
import org.bukkit.entity.Player

class SecondReward : IReward {
    private val cmd: String = "say %s won this reward!"

    override fun giveReward(p: Player) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd.format(p.name))
    }

    override fun getName(): String = "<main>Broadcast your name!"
    override fun getChance(): Double = 0.50
}