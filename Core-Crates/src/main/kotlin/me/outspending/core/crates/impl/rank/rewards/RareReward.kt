package me.outspending.core.crates.impl.rank.rewards

import me.outspending.core.crates.types.IReward
import me.outspending.core.helpers.FormatHelper.Companion.parse
import me.outspending.core.helpers.enums.CustomSound
import me.outspending.core.misc.items.ItemCreator
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class RareReward : IReward {
    private val item: ItemStack = ItemCreator(Material.PAPER).name("<main>Rare Reward").lore(listOf("<dark_gray>Reward", "", "<gray><u>test")).create()

    override fun giveReward(p: Player) {
        p.inventory.addItem(item)
        Bukkit.broadcast("<second>${p.name} <gray>has won ${getName()}".parse(true))
        CustomSound.Success(pitch = 1.65F).playSound(p)
    }

    override fun getName(): String = "<yellow>Rare Rank"
    override fun getChance(): Double = 0.20
    override fun getItem(): ItemStack = item
}