package me.outspending.core.crates.types

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

interface IReward {

    fun giveReward(p: Player)
    fun getName(): String
    fun getChance(): Double
    fun getItem(): ItemStack

}