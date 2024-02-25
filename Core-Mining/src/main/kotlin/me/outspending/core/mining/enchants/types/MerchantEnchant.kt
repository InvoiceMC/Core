package me.outspending.core.mining.enchants.types

import me.outspending.core.data.player.PlayerData
import me.outspending.core.mining.enchants.EnchantResult
import me.outspending.core.mining.enchants.PickaxeEnchant
import me.outspending.core.pmines.PrivateMine
import net.minecraft.server.network.ServerGamePacketListenerImpl
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.persistence.PersistentDataContainer

class MerchantEnchant : PickaxeEnchant() {
    override fun getEnchantName(): String = "merchant"
    override fun getDescription(): String = "Chance to find money whilst mining."
    override fun getEnchantItem(): Material = Material.DRIED_KELP_BLOCK
    override fun getInitialCost(): Float = 100.0f
    override fun getIncreaseProgression(): Float = 0.5f
    override fun getMaxEnchantmentLevel(): Int = 25000
    override fun getEnchantmentChance(enchantmentLevel: Int): Float = 0.0f // This enchant is always 100% therefore the chance is 0.0

    override fun execute(
        player: Player,
        playerData: PlayerData,
        playerConnection: ServerGamePacketListenerImpl,
        blockLocation: Location,
        dataContainer: PersistentDataContainer,
        enchantmentLevel: Int,
        mine: PrivateMine
    ): EnchantResult {
        if (enchantmentLevel == 0) return EnchantResult()

        val amount = RANDOM.nextDouble((100.0 * enchantmentLevel), (250.0 * enchantmentLevel))
        return EnchantResult(amount)
    }
}
