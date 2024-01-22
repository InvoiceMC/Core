package me.outspending.core.enchants.types

import me.outspending.core.enchants.EnchantResult
import me.outspending.core.enchants.PickaxeEnchant
import me.outspending.core.storage.PlayerData
import me.outspending.core.utils.Utilities.format
import me.outspending.core.utils.Utilities.toComponent
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.persistence.PersistentDataContainer
import kotlin.random.Random

class GoldFinderEnchant : PickaxeEnchant {

    private val DEFAULT_CHANCE = 0.0002

    override fun getEnchantName(): String = "goldfinder"

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
        // Check if enchant should be executed
        if (random.nextDouble() > getEnchantmentChance(enchantmentLevel)) return EnchantResult()

        // Execute enchant
        val goldFinderAmount: Int =
            random.nextInt((500 * enchantmentLevel), (2500 * enchantmentLevel))
        player.sendMessage(
            "<gold><bold>ɢᴏʟᴅꜰɪɴᴅᴇʀ <dark_gray>➜</bold> <white>you've found <yellow>${goldFinderAmount.format()} <gold>ɢᴏʟᴅ <white>whilst mining!"
                .toComponent()
        )

        return EnchantResult(goldFinderAmount.toDouble(), goldFinderAmount, 0)
    }
}
