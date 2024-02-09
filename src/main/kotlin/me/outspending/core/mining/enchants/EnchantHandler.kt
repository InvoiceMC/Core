package me.outspending.core.mining.enchants

import me.outspending.core.Utilities.getConnection
import me.outspending.core.mining.enchants.types.*
import me.outspending.core.storage.data.PlayerData
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.persistence.PersistentDataContainer
import kotlin.random.Random

object EnchantHandler {
    private val PICKAXE_ENCHANTS: List<PickaxeEnchant> =
        listOf(
            GoldFinderEnchant(),
            JackhammerEnchant(),
            MerchantEnchant(),
            ExplosionEnchant(),
            LevelFinderEnchant(),
            XPFinderEnchant()
        )

    fun executeAllEnchants(
        player: Player,
        playerData: PlayerData,
        blockLocation: Location,
        random: Random
    ): EnchantResult {
        val itemMeta = player.inventory.itemInMainHand.itemMeta
        val dataContainer: PersistentDataContainer = itemMeta.persistentDataContainer

        val connection = player.getConnection()!!
        val enchantResult = EnchantResult()
        PICKAXE_ENCHANTS.forEach { enchant ->
            val enchantmentLevel: Int = enchant.getEnchantmentLevel(dataContainer)
            if (enchantmentLevel > 0) {
                val result: EnchantResult =
                    enchant.execute(
                        player,
                        playerData,
                        connection,
                        dataContainer,
                        enchantmentLevel,
                        blockLocation,
                        random
                    )

                enchantResult += result
            }
        }

        return enchantResult
    }
}
