package me.outspending.core.crates.impl.test.rewards

import me.outspending.core.crates.types.IReward
import me.outspending.core.misc.items.ItemCreator
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class TestReward : IReward {

    private val item = ItemCreator(Material.DIAMOND).name("<main>Test Reward").create()

    override fun giveReward(p: Player) {
        p.inventory.addItem(item)
    }

    override fun getName(): String {
        return "<main>1x Diamond"
    }

    override fun getChance(): Double = 0.50
    override fun getItem(): ItemStack = item
}