package me.outspending.core.enchants.types

import me.outspending.core.enchants.EnchantResult
import me.outspending.core.enchants.PickaxeEnchant
import me.outspending.core.storage.data.PlayerData
import org.bukkit.Location
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.entity.TNTPrimed
import org.bukkit.persistence.PersistentDataContainer
import kotlin.random.Random

class ExplosionEnchant : PickaxeEnchant {
    private val DEFAULT_CHANCE = 0.0002

    override fun getEnchantName(): String = "explosion"

    override fun getInitialCost(): Double = 100.0

    override fun getMaxEnchantmentLevel(): Int = 25000

    override fun getEnchantmentChance(enchantLevel: Int): Double = DEFAULT_CHANCE * enchantLevel

    override fun execute(
        player: Player,
        playerData: PlayerData,
        dataContainer: PersistentDataContainer,
        enchantmentLevel: Int,
        blockLocation: Location,
        random: Random
    ): EnchantResult {
        if (random.nextDouble() > getEnchantmentChance(enchantmentLevel)) return EnchantResult()

        val entity: TNTPrimed =
            player.world.spawnEntity(
                blockLocation.clone().add(0.0, 1.5, 0.0),
                EntityType.PRIMED_TNT
            ) as TNTPrimed

        entity.fuseTicks = 25
        entity.source = player

        return EnchantResult()
    }
}
