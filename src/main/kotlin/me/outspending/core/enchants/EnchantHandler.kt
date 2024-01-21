package me.outspending.core.enchants

import me.outspending.core.storage.PlayerData
import me.outspending.core.utils.PersistentUtils
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.persistence.PersistentDataContainer
import kotlin.random.Random

object EnchantHandler {
    private val PICKAXE_ENCHANTS: List<PickaxeEnchant> = listOf(GoldFinderEnchant())

    fun executeAllEnchants(
        player: Player,
        playerData: PlayerData,
        blockLocation: Location,
        random: Random
    ): Pair<Double, Int> {
        val dataContainer: PersistentDataContainer =
            PersistentUtils.getPersistentData(player.inventory.itemInMainHand.itemMeta)

        var money = 0.0
        var gold = 0
        PICKAXE_ENCHANTS.forEach { enchant ->
            val enchantmentLevel: Int = enchant.getEnchantmentLevel(dataContainer)
            if (enchantmentLevel > 0) {
                val (money1, gold1) =
                    enchant.execute(
                        player,
                        playerData,
                        dataContainer,
                        enchantmentLevel,
                        blockLocation,
                        random
                    )
                if ((money1 > 0.0) or (gold1 > 0)) {
                    money += money1
                    gold += gold1
                }
            }
        }

        return (money to gold)
    }
}
