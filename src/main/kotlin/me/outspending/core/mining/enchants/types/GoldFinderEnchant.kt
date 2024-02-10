package me.outspending.core.mining.enchants.types

import me.outspending.core.Utilities.format
import me.outspending.core.Utilities.toTinyString
import me.outspending.core.mining.enchants.EnchantResult
import me.outspending.core.mining.enchants.PickaxeEnchant
import me.outspending.core.misc.helpers.FormatHelper.Companion.parse
import me.outspending.core.storage.data.PlayerData
import net.minecraft.server.network.ServerGamePacketListenerImpl
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.persistence.PersistentDataContainer
import kotlin.random.Random

class GoldFinderEnchant : PickaxeEnchant {

    private val DEFAULT_CHANCE = 0.0002

    override fun getEnchantName(): String = "goldfinder"

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
        // Check if enchant should be executed
        if (random.nextDouble() > getEnchantmentChance(enchantmentLevel)) return EnchantResult()

        // Execute enchant
        val goldFinderAmount: Int =
            random.nextInt((500 * enchantmentLevel), (2500 * enchantmentLevel))
        player.sendMessage(
            "<main><b>ɢᴏʟᴅꜰɪɴᴅᴇʀ <dark_gray>➜</b> <white>you've found <yellow>⛁${goldFinderAmount.format().toTinyString()} <white>whilst mining!"
                .parse()
        )

        return EnchantResult(goldFinderAmount.toDouble(), goldFinderAmount)
    }
}
