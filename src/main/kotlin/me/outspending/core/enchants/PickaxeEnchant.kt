package me.outspending.core.enchants

import me.outspending.core.storage.data.PlayerData
import net.minecraft.server.network.ServerGamePacketListenerImpl
import org.bukkit.Location
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.persistence.PersistentDataType
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

interface PickaxeEnchant {

    fun getEnchantName(): String

    fun getInitialCost(): Double = 100.0

    fun getIncreaseProgression(): Double = 0.5

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
        random: Random
    ): EnchantResult

    fun getBlockCount(loc1: Location, loc2: Location): BlockCountLocation {
        val minX = min(loc1.x, loc2.x)
        val minY = min(loc1.y, loc2.y)
        val minZ = min(loc1.z, loc2.z)

        val maxX = max(loc1.x, loc2.x)
        val maxY = max(loc1.y, loc2.y)
        val maxZ = max(loc1.z, loc2.z)

        return BlockCountLocation(minX.toInt(), minY.toInt(), minZ.toInt(), maxX.toInt(), maxY.toInt(), maxZ.toInt(), ((maxX - minX) * (maxY - minY) * (maxZ - minZ)).toInt())
    }

    data class BlockCountLocation(val minX: Int, val minY: Int, val minZ: Int, val maxX: Int, val maxY: Int, val maxZ: Int, val blockCount: Int)
}
