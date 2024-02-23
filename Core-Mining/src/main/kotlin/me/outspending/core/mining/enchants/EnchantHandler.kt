package me.outspending.core.mining.enchants

import me.outspending.core.data.player.PlayerData
import me.outspending.core.mining.Extensions.getConnection
import me.outspending.core.pmines.Mine
import me.outspending.core.pmines.PrivateMine
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.util.BoundingBox
import org.reflections.Reflections
import kotlin.random.Random

const val ENCHANTS_PACKAGE = "me.outspending.core.mining.enchants.types"

object EnchantHandler {
    val pickaxeEnchants: List<PickaxeEnchant> =
        Reflections(ENCHANTS_PACKAGE)
            .getSubTypesOf(PickaxeEnchant::class.java)
            .map { it.getDeclaredConstructor().newInstance() as PickaxeEnchant }

    fun executeAllEnchants(
        player: Player,
        playerData: PlayerData,
        blockLocation: Location,
        mine: PrivateMine,
        random: Random
    ): EnchantResult {
        val itemMeta = player.inventory.itemInMainHand.itemMeta
        val dataContainer: PersistentDataContainer = itemMeta.persistentDataContainer

        val connection = player.getConnection()
        val enchantResult = EnchantResult()
        pickaxeEnchants.forEach { enchant ->
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
                        mine,
                        random
                    )

                enchantResult += result
            }
        }

        return enchantResult
    }
}
