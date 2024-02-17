package me.outspending.core.mining.enchants.types

import me.outspending.core.Utilities.delay
import me.outspending.core.Utilities.toComponent
import me.outspending.core.mining.enchants.EnchantResult
import me.outspending.core.mining.enchants.PickaxeEnchant
import me.outspending.core.storage.data.PlayerData
import net.kyori.adventure.title.Title
import net.minecraft.server.network.ServerGamePacketListenerImpl
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.persistence.PersistentDataContainer
import kotlin.random.Random

class LevelFinderEnchant : PickaxeEnchant {
    private val DEFAULT_CHANCE = 0.0002

    override fun getEnchantName(): String = "levelfinder"
    override fun getDescription(): String = "Chance to find levels whilst mining."
    override fun getEnchantItem(): Material = Material.REDSTONE_TORCH
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
        random: Random
    ): EnchantResult {
        if (random.nextDouble() > getEnchantmentChance(enchantmentLevel)) return EnchantResult()
        if (player.level >= (100 + (25 * playerData.prestige))) return EnchantResult()

        val amount = random.nextInt(1, 7)
        player.level += amount

        delay(2) {
            player.showTitle(
                Title.title(
                    "<main><b>ʟᴇᴠᴇʟꜰɪɴᴅᴇʀ".toComponent(),
                    "<gray>You've found <main>${amount}</main> <gray>levels!".toComponent()
                )
            )
        }

        return EnchantResult()
    }
}