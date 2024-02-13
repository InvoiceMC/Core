package me.outspending.core.gameplay.crates.types

import org.bukkit.entity.Player

interface IReward {

    fun giveReward(p: Player)
    fun getName(): String
    fun getChance(): Double

}