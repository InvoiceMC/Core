package me.outspending.core.mining.enchants.types

import me.outspending.core.center
import me.outspending.core.data.player.PlayerData
import me.outspending.core.helpers.FormatHelper.Companion.parse
import me.outspending.core.helpers.enums.CustomSound
import me.outspending.core.mining.enchants.EnchantResult
import me.outspending.core.mining.enchants.PickaxeEnchant
import me.outspending.core.mining.shapes.CuboidShape
import me.outspending.core.mining.shapes.SphereShape
import me.outspending.core.pmines.PrivateMine
import me.outspending.core.regex
import net.minecraft.server.network.ServerGamePacketListenerImpl
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.persistence.PersistentDataContainer

class NukeEnchant : PickaxeEnchant() {
    private val DEFAULT_CHANCE = 0.00002f

    override fun getEnchantName(): String = "nuke"
    override fun getDescription(): String = "Breaks all blocks in a radius of 25"
    override fun getEnchantItem(): Material = Material.DRAGON_EGG
    override fun getInitialCost(): Float = 500000f
    override fun getIncreaseProgression(): Float = 1.5f
    override fun getMaxEnchantmentLevel(): Int = 1000
    override fun getEnchantmentChance(enchantmentLevel: Int): Float = DEFAULT_CHANCE * enchantmentLevel

    override fun execute(
        player: Player,
        playerData: PlayerData,
        playerConnection: ServerGamePacketListenerImpl,
        blockLocation: Location,
        dataContainer: PersistentDataContainer,
        enchantmentLevel: Int,
        mine: PrivateMine
    ): EnchantResult {
        if (RANDOM.nextDouble(100.0) > getEnchantmentChance(enchantmentLevel)) return EnchantResult()

        val blockCount = SphereShape(25).process(mine, blockLocation)

        val moneyAmount: Double = RANDOM.nextDouble(10.0, 25.0) * blockCount
        val coinsAmount: Int = (moneyAmount / 5).toInt()

        player.sendActionBar(
            "<second>Nuke <gray>Has procced and broke <second>${blockCount.regex()} <gray>blocks".parse(true)
        )

        return EnchantResult(moneyAmount, coinsAmount, (blockCount / 4), blockCount)
    }
}