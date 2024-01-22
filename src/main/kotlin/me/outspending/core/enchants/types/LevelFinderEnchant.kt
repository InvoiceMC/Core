package me.outspending.core.enchants.types

import kotlin.random.Random
import me.outspending.core.enchants.EnchantResult
import me.outspending.core.enchants.PickaxeEnchant
import me.outspending.core.storage.PlayerData
import me.outspending.core.utils.Utilities.Companion.toComponent
import me.outspending.core.utils.delay
import net.kyori.adventure.title.Title
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.persistence.PersistentDataContainer

class LevelFinderEnchant : PickaxeEnchant {
    private val DEFAULT_CHANCE = 0.0002

    override fun getEnchantName(): String = "levelfinder"

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
        if (player.level >= (100 + (25 * playerData.prestige))) return EnchantResult()

        val amount = random.nextInt(1, 7)
        player.level += amount

        delay(2) {
            player.showTitle(
                Title.title(
                    "<gradient:#e08a19:#e8b36d><b>LEVELFINDER".toComponent(),
                    "<gray>You've found <color:#e8b36d>${amount}</color> <gray>levels!".toComponent()
                )
            )
        }

        return EnchantResult()
    }
}
