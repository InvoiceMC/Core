package me.outspending.core.mining.enchants.types

import me.outspending.core.data.player.PlayerData
import me.outspending.core.format
import me.outspending.core.helpers.FormatHelper.Companion.parse
import me.outspending.core.mining.enchants.EnchantResult
import me.outspending.core.mining.enchants.PickaxeEnchant
import me.outspending.core.pmines.PrivateMine
import net.minecraft.server.network.ServerGamePacketListenerImpl
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.persistence.PersistentDataContainer

class GoldFinderEnchant : PickaxeEnchant() {
    private val DEFAULT_CHANCE: Float = 0.0002f

    override fun getEnchantName(): String = "goldfinder"
    override fun getDescription(): String = "Chance to find gold whilst mining."
    override fun getEnchantItem(): Material = Material.SUNFLOWER
    override fun getInitialCost(): Float = 100.0f
    override fun getIncreaseProgression(): Float = 0.5f
    override fun getMaxEnchantmentLevel(): Int = 25000
    override fun getEnchantmentChance(enchantmentLevel: Int): Float = DEFAULT_CHANCE * enchantmentLevel

    override fun execute(
        player: Player,
        playerData: PlayerData,
        playerConnection: ServerGamePacketListenerImpl,
        blockLocation: Location,
        dataContainer: PersistentDataContainer,
        enchantmentLevel: Int,
        mine: PrivateMine
    ): EnchantResult {
        // Check if enchant should be executed
        if (RANDOM.nextDouble(100.0) > getEnchantmentChance(enchantmentLevel)) return EnchantResult()

        // Execute enchant
        val goldFinderAmount: Int =
            RANDOM.nextInt((500 * enchantmentLevel), (2500 * enchantmentLevel))
        player.sendActionBar(
            "<second>Goldfinder <gray>has procced and you've found <yellow>⛁${goldFinderAmount.format()}<gray>!"
                .parse(true)
        )

        return EnchantResult(gold = goldFinderAmount)
    }
}
