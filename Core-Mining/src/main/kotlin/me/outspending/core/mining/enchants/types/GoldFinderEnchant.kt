package me.outspending.core.mining.enchants.types

import me.outspending.core.Utilities.format
import me.outspending.core.data.player.PlayerData
import me.outspending.core.helpers.FormatHelper.Companion.parse
import me.outspending.core.mining.enchants.EnchantResult
import me.outspending.core.mining.enchants.PickaxeEnchant
import net.minecraft.server.network.ServerGamePacketListenerImpl
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.util.BoundingBox
import kotlin.random.Random

class GoldFinderEnchant : PickaxeEnchant {

    private val DEFAULT_CHANCE = 0.0002
    override fun getEnchantName(): String = "goldfinder"
    override fun getDescription(): String = "Chance to find gold whilst mining."
    override fun getEnchantItem(): Material = Material.SUNFLOWER
    override fun getInitialCost(): Double = 100.0
    override fun getMaxEnchantmentLevel(): Int = 25000
    override fun getEnchantmentChance(enchantLevel: Int): Double = DEFAULT_CHANCE * enchantLevel

    override fun execute(
        player: Player,
        playerData: PlayerData,
        playerConnection: ServerGamePacketListenerImpl,
        dataContainer: PersistentDataContainer,
        enchantmentLevel: Int,
        blockLocation: Location,
        region: BoundingBox,
        random: Random
    ): EnchantResult {
        // Check if enchant should be executed
        if (random.nextDouble() > getEnchantmentChance(enchantmentLevel)) return EnchantResult()

        // Execute enchant
        val goldFinderAmount: Int =
            random.nextInt((500 * enchantmentLevel), (2500 * enchantmentLevel))
        player.sendActionBar(
            "<second>Goldfinder <gray>has procced and you've found <yellow>⛁${goldFinderAmount.format()}<gray>!"
                .parse(true)
        )

        return EnchantResult(gold = goldFinderAmount)
    }
}
