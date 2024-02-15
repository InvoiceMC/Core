package me.outspending.core.gameplay.crates.impl.rank.rewards

import me.outspending.core.gameplay.crates.types.IReward
import me.outspending.core.misc.items.ItemCreator
import org.bukkit.Material
import org.bukkit.entity.Player

class CommonReward : IReward {

    private val item = ItemCreator(Material.PAPER).name("<main>Common Reward").create()

    override fun giveReward(p: Player) {
        p.inventory.addItem(item)
    }

    override fun getName(): String {
        return "<light_purple>Common Rank"
    }

    override fun getChance(): Double = 0.80
}