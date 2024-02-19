package me.outspending.core.crates.impl.test.rewards

import me.outspending.core.misc.items.ItemCreator
import me.outspending.core.crates.types.IReward
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class SecondReward : IReward {
    private val cmd: String = "say %s won this reward!"
    private val item: ItemStack = ItemCreator(Material.PAPER).name("<main>Broadcast your name!").create()

    override fun giveReward(p: Player) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd.format(p.name))
    }

    override fun getName(): String = "<main>Broadcast your name!"
    override fun getChance(): Double = 0.50
    override fun getItem(): ItemStack = item
}