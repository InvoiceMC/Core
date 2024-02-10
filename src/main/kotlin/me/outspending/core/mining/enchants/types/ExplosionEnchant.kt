package me.outspending.core.mining.enchants.types

import me.outspending.core.mining.MineUtils
import me.outspending.core.mining.enchants.EnchantResult
import me.outspending.core.mining.enchants.PickaxeEnchant
import me.outspending.core.mining.shapes.SphereShape
import me.outspending.core.storage.data.PlayerData
import net.minecraft.server.network.ServerGamePacketListenerImpl
import org.bukkit.Location
import org.bukkit.entity.Player
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
        playerConnection: ServerGamePacketListenerImpl,
        dataContainer: PersistentDataContainer,
        enchantmentLevel: Int,
        blockLocation: Location,
        random: Random
    ): EnchantResult {
        if (random.nextDouble() > getEnchantmentChance(enchantmentLevel)) return EnchantResult()

        val blockCount = MineUtils.setBlocks(
            player,
            blockLocation,

            shape = SphereShape(4),
            syncPackets = true
        )

        val moneyAmount: Double = random.nextDouble(10.0, 25.0) * blockCount
        val coinsAmount: Int = (moneyAmount / 5).toInt()

        return EnchantResult(moneyAmount, coinsAmount, blockCount, blockCount)
    }
}
