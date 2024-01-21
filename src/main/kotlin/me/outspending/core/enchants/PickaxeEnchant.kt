package me.outspending.core.enchants

import me.outspending.core.storage.PlayerData
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.persistence.PersistentDataContainer
import kotlin.random.Random

interface PickaxeEnchant {

    fun getEnchantName(): String

    fun getInitialCost(): Double

    fun getMaxEnchantmentLevel(): Int

    fun getEnchantmentChance(enchantLevel: Int): Double

    fun getEnchantmentLevel(itemContainer: PersistentDataContainer): Int

    fun execute(
        player: Player,
        playerData: PlayerData,
        dataContainer: PersistentDataContainer,
        enchantmentLevel: Int,
        blockLocation: Location,
        random: Random
    ): Pair<Double, Int>
}
