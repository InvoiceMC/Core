package me.outspending.core.mining.enchants.types

import me.outspending.core.data.player.PlayerData
import me.outspending.core.helpers.enums.CustomSound
import me.outspending.core.mining.enchants.EnchantResult
import me.outspending.core.mining.enchants.PickaxeEnchant
import me.outspending.core.mining.shapes.SphereShape
import me.outspending.core.pmines.PrivateMine
import net.minecraft.server.network.ServerGamePacketListenerImpl
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.persistence.PersistentDataContainer

class ExplosionEnchant : PickaxeEnchant() {
    private val DEFAULT_CHANCE: Float = 0.0002f

    override fun getEnchantName(): String = "explosion"
    override fun getDescription(): String = "Chance to explode blocks in a radius of 4."
    override fun getEnchantItem(): Material = Material.TNT
    override fun getInitialCost(): Float = 100.0f
    override fun getIncreaseProgression(): Float = 0.5f
    override fun getMaxEnchantmentLevel(): Int = 25000
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
        if (RANDOM.nextDouble() > getEnchantmentChance(enchantmentLevel)) return EnchantResult()
        val blockCount: Int = SphereShape(4).process(mine, blockLocation)

        player.spawnParticle(Particle.EXPLOSION_LARGE, blockLocation, 1)
        CustomSound.Custom(Sound.ENTITY_GENERIC_EXPLODE, 0.5f, 1.5f).playSound(player)

        val moneyAmount: Double = RANDOM.nextDouble(10.0, 25.0) * blockCount
        val coinsAmount: Int = (moneyAmount / 5).toInt()

        return EnchantResult(moneyAmount, coinsAmount, blockCount, blockCount)
    }
}
