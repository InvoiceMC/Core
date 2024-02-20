package me.outspending.core.mining.enchants

import me.outspending.core.data.player.PlayerData
import me.outspending.core.pmines.Mine
import net.minecraft.server.network.ServerGamePacketListenerImpl
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.persistence.PersistentDataType
import org.bukkit.util.BoundingBox
import kotlin.random.Random

interface PickaxeEnchant {

    fun getEnchantName(): String
    fun getDescription(): String
    fun getEnchantItem(): Material
    fun getInitialCost(): Double = 100.0
    fun getIncreaseProgression(): Double = 1.0
    fun getMaxEnchantmentLevel(): Int = 25000
    fun getEnchantmentChance(enchantLevel: Int): Double

    fun getEnchantmentLevel(itemContainer: PersistentDataContainer): Int {
        return itemContainer.getOrDefault(
            NamespacedKey("enchant", getEnchantName()),
            PersistentDataType.INTEGER,
            0
        )
    }

    fun execute(
        player: Player,
        playerData: PlayerData,
        playerConnection: ServerGamePacketListenerImpl,
        dataContainer: PersistentDataContainer,
        enchantmentLevel: Int,
        blockLocation: Location,
        region: BoundingBox,
        random: Random
    ): EnchantResult
}
