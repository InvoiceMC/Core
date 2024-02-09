package me.outspending.core.mining.enchants

import me.outspending.core.misc.helpers.FormatHelper.Companion.parse
import me.outspending.core.storage.data.PlayerData
import net.kyori.adventure.title.Title
import net.minecraft.server.network.ServerGamePacketListenerImpl
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.persistence.PersistentDataContainer
import kotlin.random.Random

class XPFinderEnchant : PickaxeEnchant {
    private val DEFAULT_CHANCE = 0.0002

    override fun getEnchantName(): String = "xpfinder"

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
        if (player.level >= (100 + (25 * playerData.prestige))) return EnchantResult()

        val xp = random.nextInt((50 * playerData.prestige), (250 * playerData.prestige))

        player.showTitle(
            Title.title(
                "<main><b>XPFINDER".parse(),
                "<gray>You've found <main>${xp}</main> <gray>xp!".parse()
            )
        )

        return EnchantResult(0.0, 0, xp)
    }
}