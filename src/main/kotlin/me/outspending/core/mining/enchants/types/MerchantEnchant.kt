package me.outspending.core.mining.enchants.types

import me.outspending.core.mining.enchants.EnchantResult
import me.outspending.core.mining.enchants.PickaxeEnchant
import me.outspending.core.storage.data.PlayerData
import net.minecraft.server.network.ServerGamePacketListenerImpl
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.persistence.PersistentDataContainer
import kotlin.random.Random

class MerchantEnchant : PickaxeEnchant {
    override fun getEnchantName(): String = "merchant"

    override fun getInitialCost(): Double = 100.0

    override fun getMaxEnchantmentLevel(): Int = 25000

    // This enchant is always 100% therefore the chance is 0.0
    override fun getEnchantmentChance(enchantLevel: Int): Double = 0.0

    override fun execute(
        player: Player,
        playerData: PlayerData,
        playerConnection: ServerGamePacketListenerImpl,
        dataContainer: PersistentDataContainer,
        enchantmentLevel: Int,
        blockLocation: Location,
        random: Random
    ): EnchantResult {
        if (enchantmentLevel == 0) return EnchantResult()

        val amount = random.nextDouble((100.0 * enchantmentLevel), (250.0 * enchantmentLevel))
        return EnchantResult(amount, 0, 0)
    }
}