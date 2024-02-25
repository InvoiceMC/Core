package me.outspending.core.mining.enchants.types

import me.outspending.core.data.player.PlayerData
import me.outspending.core.delay
import me.outspending.core.helpers.FormatHelper.Companion.parse
import me.outspending.core.mining.enchants.EnchantResult
import me.outspending.core.mining.enchants.PickaxeEnchant
import me.outspending.core.pmines.PrivateMine
import net.minecraft.server.network.ServerGamePacketListenerImpl
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.persistence.PersistentDataContainer

class LevelFinderEnchant : PickaxeEnchant() {
    private val DEFAULT_CHANCE: Float = 0.0002f

    override fun getEnchantName(): String = "levelfinder"
    override fun getDescription(): String = "Chance to find levels whilst mining."
    override fun getEnchantItem(): Material = Material.REDSTONE_TORCH
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
        if (RANDOM.nextDouble() > getEnchantmentChance(enchantmentLevel)) return EnchantResult()
        if (player.level >= (100 + (25 * playerData.prestige))) return EnchantResult()

        val amount = RANDOM.nextInt(1, 7)
        player.level += amount

        delay(2) {
            player.sendActionBar(
                "<second>LevelFinder <gray>has procced and gave you <second>${amount} <gray>levels!".parse(true)
            )
        }

        return EnchantResult()
    }
}
