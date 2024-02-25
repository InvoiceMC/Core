package me.outspending.core.mining.enchants

import me.outspending.core.data.player.PlayerData
import me.outspending.core.pmines.PrivateMine
import net.minecraft.server.network.ServerGamePacketListenerImpl
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.persistence.PersistentDataType
import java.util.concurrent.ThreadLocalRandom

data class EnchantResult(
    var money: Double = 0.0,
    var gold: Int = 1,
    var xp: Int = 0,
    var blocks: Int = 0
) {
    operator fun plusAssign(enchantResult: EnchantResult) {
        money += enchantResult.money
        gold += enchantResult.gold
        xp += enchantResult.xp
        blocks += enchantResult.blocks
    }
}

abstract class PickaxeEnchant {
    companion object {
        val RANDOM = ThreadLocalRandom.current()
    }

    private val enchantKey = NamespacedKey("enchant", getEnchantName())

    abstract fun getEnchantName(): String
    abstract fun getDescription(): String
    abstract fun getEnchantItem(): Material
    abstract fun getInitialCost(): Float
    abstract fun getIncreaseProgression(): Float
    abstract fun getMaxEnchantmentLevel(): Int
    abstract fun getEnchantmentChance(enchantmentLevel: Int): Float
    abstract fun execute(
        player: Player,
        playerData: PlayerData,
        playerConnection: ServerGamePacketListenerImpl,
        blockLocation: Location,
        dataContainer: PersistentDataContainer,
        enchantmentLevel: Int,
        mine: PrivateMine,
    ): EnchantResult

    fun getEnchantmentLevel(itemContainer: PersistentDataContainer): Int? =
        itemContainer.get(enchantKey, PersistentDataType.INTEGER)
}
