package me.outspending.core.enchants.types

import me.outspending.core.enchants.EnchantResult
import me.outspending.core.enchants.PickaxeEnchant
import me.outspending.core.storage.data.PlayerData
import me.outspending.core.utils.MineUtils
import me.outspending.core.utils.Utilities.regex
import me.outspending.core.utils.helpers.FormatHelper.Companion.parse
import me.outspending.core.utils.shapes.CuboidShape
import net.kyori.adventure.title.Title
import net.minecraft.server.network.ServerGamePacketListenerImpl
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.util.Vector
import kotlin.random.Random

class JackhammerEnchant : PickaxeEnchant {

    private val DEFAULT_CHANCE = 0.0002

    override fun getEnchantName(): String = "jackhammer"

    override fun getInitialCost(): Double = 1000.0

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

        val vec1 = Vector(5, 0, 5)
        val vec2 = Vector(-5, 0, -5)

        val blockCount =
            MineUtils.setBlocks(
                player,
                blockLocation,
                CuboidShape(vec1, vec2),

                syncPackets = true
            )

        val moneyAmount: Double = random.nextDouble(10.0, 25.0) * blockCount
        val coinsAmount: Int = (moneyAmount / 5).toInt()

        player.showTitle(
            Title.title(
                "<main><b>JACKHAMMER".parse(),
                "<gray>Blocks Broken: <main>${blockCount.regex()}".parse()
            )
        )

        return EnchantResult(moneyAmount, coinsAmount, blockCount)
    }
}
