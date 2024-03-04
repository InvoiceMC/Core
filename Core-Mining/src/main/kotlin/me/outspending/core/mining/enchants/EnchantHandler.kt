package me.outspending.core.mining.enchants

import me.outspending.core.data.player.PlayerData
import me.outspending.core.helpers.FormatHelper.Companion.parse
import me.outspending.core.mining.MetaStorage
import me.outspending.core.mining.getConnection
import me.outspending.core.pmines.PrivateMine
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.persistence.PersistentDataContainer
import org.reflections.Reflections
import kotlin.time.measureTime

const val ENCHANTS_PACKAGE = "me.outspending.core.mining.enchants.types"

object EnchantHandler {
    val pickaxeEnchants: List<PickaxeEnchant> =
        Reflections(ENCHANTS_PACKAGE)
            .getSubTypesOf(PickaxeEnchant::class.java)
            .map { it.getDeclaredConstructor().newInstance() as PickaxeEnchant }

    fun executeAllEnchants(
        player: Player,
        playerData: PlayerData,
        metaStorage: MetaStorage,
        blockLocation: Location,
        mine: PrivateMine
    ): EnchantResult {
        val dataContainer: PersistentDataContainer = metaStorage.data

        val connection = player.getConnection()
        val enchantResult = EnchantResult()
        pickaxeEnchants
            .mapNotNull { enchant ->
                val enchantLevel = enchant.getEnchantmentLevel(dataContainer)

                if (enchantLevel != null && enchantLevel > 0) enchant to enchantLevel else null
            }
            .forEach { (enchant, level) ->
                val time = measureTime {
                    val result: EnchantResult =
                        enchant.execute(
                            player,
                            playerData,
                            connection,
                            blockLocation,
                            dataContainer,
                            level,
                            mine
                        )

                    enchantResult += result
                }
                Bukkit.broadcast("Finished in <gold>$time".parse())
            }

        return enchantResult
    }
}
