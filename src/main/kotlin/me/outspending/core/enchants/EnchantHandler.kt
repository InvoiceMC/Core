package me.outspending.core.enchants

import me.outspending.core.enchants.types.*
import me.outspending.core.storage.data.PlayerData
import me.outspending.core.utils.PersistentUtils
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
        val dataContainer: PersistentDataContainer =
            PersistentUtils.getPersistentData(player.inventory.itemInMainHand.itemMeta)

        var enchantResult = EnchantResult()
        PICKAXE_ENCHANTS.forEach { enchant ->
            val enchantmentLevel: Int = enchant.getEnchantmentLevel(dataContainer)
            if (enchantmentLevel > 0) {
                val result: EnchantResult =
                    enchant.execute(
                        player,
                        playerData,
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
