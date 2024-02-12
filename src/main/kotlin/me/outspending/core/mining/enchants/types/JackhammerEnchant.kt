package me.outspending.core.mining.enchants.types

import me.outspending.core.Utilities.regex
import me.outspending.core.mining.MineUtils
import me.outspending.core.mining.enchants.EnchantResult
import me.outspending.core.mining.enchants.PickaxeEnchant
import me.outspending.core.mining.shapes.CuboidShape
import me.outspending.core.misc.helpers.FormatHelper.Companion.parse
import me.outspending.core.storage.data.PlayerData
import net.kyori.adventure.title.Title
import net.minecraft.server.network.ServerGamePacketListenerImpl
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.util.Vector
import kotlin.random.Random

class JackhammerEnchant : PickaxeEnchant {

    private val DEFAULT_CHANCE = 0.0002

    override fun getEnchantName(): String = "jackhammer"
    override fun getDescription(): String = "Chance to break blocks in a flat radius of 5."
    override fun getEnchantItem(): Material = Material.HOPPER
    override fun getInitialCost(): Double = 1000.0
    override fun getMaxEnchantmentLevel(): Int = 10
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
                "<main><b>ᴊᴀᴄᴋʜᴀᴍᴍᴇʀ".parse(),
                "<gray>Blocks Broken: <main>${blockCount.regex()}".parse()
            )
        )

        return EnchantResult(moneyAmount, coinsAmount, blockCount, blockCount)
    }
}
