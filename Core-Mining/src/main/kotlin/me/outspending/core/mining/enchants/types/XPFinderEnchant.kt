package me.outspending.core.mining.enchants.types

import me.outspending.core.data.player.PlayerData
import me.outspending.core.helpers.FormatHelper.Companion.parse
import me.outspending.core.mining.enchants.EnchantResult
import me.outspending.core.mining.enchants.PickaxeEnchant
import net.kyori.adventure.title.Title
import net.minecraft.server.network.ServerGamePacketListenerImpl
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.persistence.PersistentDataContainer
import kotlin.random.Random

class XPFinderEnchant : PickaxeEnchant {
    private val DEFAULT_CHANCE = 0.0002

    override fun getEnchantName(): String = "xpfinder"
    override fun getDescription(): String = "Chance to find xp whilst mining."
    override fun getEnchantItem(): Material = Material.EXPERIENCE_BOTTLE
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
                "<main><b>xᴘꜰɪɴᴅᴇʀ".parse(),
                "<gray>You've found <main>$xp</main> <gray>xp!".parse()
            )
        )

        return EnchantResult(xp = xp)
    }
}