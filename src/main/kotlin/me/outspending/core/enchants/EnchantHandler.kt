package me.outspending.core.enchants

import me.outspending.core.storage.PlayerData
import me.outspending.core.utils.PersistentUtils
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.persistence.PersistentDataContainer
import kotlin.random.Random

object EnchantHandler {
    private val PICKAXE_ENCHANTS: List<PickaxeEnchant> =
        listOf(GoldFinderEnchant(), JackhammerEnchant())

    fun executeAllEnchants(
        player: Player,
        playerData: PlayerData,
        blockLocation: Location,
        random: Random
    ): EnchantResult {
        val dataContainer: PersistentDataContainer =
            PersistentUtils.getPersistentData(player.inventory.itemInMainHand.itemMeta)

        val enchantResult = EnchantResult()
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

                enchantResult.add(result)
            }
        }

        return enchantResult
    }
}
