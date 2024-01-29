package me.outspending.core.enchants

import me.outspending.core.storage.data.PlayerData
import org.bukkit.Location
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.persistence.PersistentDataType
import kotlin.random.Random

interface PickaxeEnchant {

    fun getEnchantName(): String

    fun getInitialCost(): Double = 100.0

    fun getIncreaseProgression(): Double = 0.5

    fun getMaxEnchantmentLevel(): Int = 25000

    fun getEnchantmentChance(enchantLevel: Int): Double

    fun getEnchantmentLevel(itemContainer: PersistentDataContainer): Int {
        return itemContainer.getOrDefault(
            NamespacedKey("enchant", getEnchantName()),
            PersistentDataType.INTEGER,
            0
        )
    }

    fun execute(
        player: Player,
        playerData: PlayerData,
        dataContainer: PersistentDataContainer,
        enchantmentLevel: Int,
        blockLocation: Location,
        random: Random
    ): EnchantResult
}
