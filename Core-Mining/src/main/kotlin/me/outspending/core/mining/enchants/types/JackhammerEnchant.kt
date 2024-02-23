package me.outspending.core.mining.enchants.types

import me.outspending.core.Utilities.center
import me.outspending.core.Utilities.regex
import me.outspending.core.data.player.PlayerData
import me.outspending.core.helpers.FormatHelper.Companion.parse
import me.outspending.core.helpers.enums.CustomSound
import me.outspending.core.mining.PacketShape
import me.outspending.core.mining.enchants.EnchantResult
import me.outspending.core.mining.enchants.PickaxeEnchant
import me.outspending.core.mining.shapes.CuboidShape
import me.outspending.core.pmines.PrivateMine
import net.minecraft.server.network.ServerGamePacketListenerImpl
import org.bukkit.*
import org.bukkit.Particle.DustOptions
import org.bukkit.entity.Player
import org.bukkit.persistence.PersistentDataContainer
import kotlin.random.Random

class JackhammerEnchant : PickaxeEnchant {

    private val DEFAULT_CHANCE = 0.0002
    private val dustOptions: DustOptions = DustOptions(Color.RED, 1f)

    override fun getEnchantName(): String = "jackhammer"
    override fun getDescription(): String = "Chance to break blocks in a flat radius of 5."
    override fun getEnchantItem(): Material = Material.HOPPER
    override fun getInitialCost(): Double = 1000.0
    override fun getMaxEnchantmentLevel(): Int = 1000
    override fun getEnchantmentChance(enchantLevel: Int): Double = DEFAULT_CHANCE * enchantLevel

    override fun execute(
        player: Player,
        playerData: PlayerData,
        playerConnection: ServerGamePacketListenerImpl,
        dataContainer: PersistentDataContainer,
        enchantmentLevel: Int,
        blockLocation: Location,
        mine: PrivateMine,
        random: Random
    ): EnchantResult {
        if (random.nextDouble() > getEnchantmentChance(enchantmentLevel)) return EnchantResult()

        val maxLoc = blockLocation.clone().add(5.0, 0.0, 5.0)
        val minLoc = blockLocation.clone().add(-5.0, 0.0, -5.0)

        val blockCount = CuboidShape(minLoc, maxLoc).process(mine, blockLocation) { location, blockData ->
            player.spawnParticle(Particle.REDSTONE, location.center(), 1, 0.0, 0.0, 0.0, 1.0, dustOptions)
        }

        val moneyAmount: Double = random.nextDouble(10.0, 25.0) * blockCount
        val coinsAmount: Int = (moneyAmount / 5).toInt()

        CustomSound.Custom(Sound.BLOCK_PISTON_CONTRACT, 1f, 1.25f).playSound(player)
        player.sendActionBar(
            "<second>Jackhammer <gray>Has procced and broke <second>${blockCount.regex()} <gray>blocks".parse(true)
        )

        return EnchantResult(moneyAmount, coinsAmount, blockCount, blockCount)
    }
}
