package me.outspending.core.enchants

import kotlin.random.Random
import me.outspending.core.storage.PlayerData
import me.outspending.core.utils.Utilities.Companion.format
import me.outspending.core.utils.Utilities.Companion.toComponent
import org.bukkit.Location
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.persistence.PersistentDataType

class GoldFinderEnchant : PickaxeEnchant {

    private val DEFAULT_CHANCE = 0.0002

    override fun getEnchantName(): String = "goldfinder"

    override fun getInitialCost(): Double = 100.0

    override fun getMaxEnchantmentLevel(): Int = 25000

    override fun getEnchantmentChance(enchantLevel: Int): Double = DEFAULT_CHANCE * enchantLevel

    override fun getEnchantmentLevel(itemContainer: PersistentDataContainer): Int {
        // Get the enchantment level from the NBT of the pickaxe aka itemContainer
        return itemContainer.getOrDefault(
            NamespacedKey("enchant", getEnchantName()),
            PersistentDataType.INTEGER,
            0
        )
    }

    override fun execute(
        player: Player,
        playerData: PlayerData,
        dataContainer: PersistentDataContainer,
        enchantmentLevel: Int,
        blockLocation: Location,
        random: Random
    ): Pair<Double, Int> {
        // Check if enchant should be executed
        if (random.nextDouble() > getEnchantmentChance(enchantmentLevel)) return (0.0 to 0)

        // Execute enchant
        val goldFinderAmount: Int =
            random.nextInt((500 * enchantmentLevel), (2500 * enchantmentLevel))
        player.sendMessage(
            "<gold><bold>ɢᴏʟᴅꜰɪɴᴅᴇʀ <dark_gray>➜</bold> <white>you've found <yellow>${goldFinderAmount.format()} <gold>ɢᴏʟᴅ <white>whilst mining!"
                .toComponent()
        )

        return (0.0 to goldFinderAmount)
    }
}
