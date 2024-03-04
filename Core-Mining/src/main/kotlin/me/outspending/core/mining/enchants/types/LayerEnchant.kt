package me.outspending.core.mining.enchants.types

import me.outspending.core.center
import me.outspending.core.data.player.PlayerData
import me.outspending.core.helpers.FormatHelper.Companion.parse
import me.outspending.core.helpers.enums.CustomSound
import me.outspending.core.mining.enchants.EnchantResult
import me.outspending.core.mining.enchants.PickaxeEnchant
import me.outspending.core.mining.shapes.CuboidShape
import me.outspending.core.pmines.PrivateMine
import me.outspending.core.regex
import net.minecraft.server.network.ServerGamePacketListenerImpl
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.persistence.PersistentDataContainer

class LayerEnchant : PickaxeEnchant() {
    private val DEFAULT_CHANCE = 0.001f

    override fun getEnchantName(): String = "layer"
    override fun getDescription(): String = "Will break an entire layer in your mine"
    override fun getEnchantItem(): Material = Material.COD
    override fun getInitialCost(): Float = 5000f
    override fun getIncreaseProgression(): Float = 1.5f
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
        if (RANDOM.nextDouble(100.0) > getEnchantmentChance(enchantmentLevel)) return EnchantResult()

        val pmine = mine.getMine()

        val (minLoc, maxLoc) = getMinAndMaxLocations(pmine.getTopLocation(), pmine.getBottomLocation())

        minLoc.y = blockLocation.y
        maxLoc.y = blockLocation.y

        val blockCount = CuboidShape(minLoc, maxLoc).process(mine, blockLocation)

        val moneyAmount: Double = RANDOM.nextDouble(10.0, 25.0) * blockCount
        val coinsAmount: Int = (moneyAmount / 5).toInt()

        CustomSound.Custom(Sound.UI_LOOM_TAKE_RESULT, 1f, 1.34f).playSound(player)
        player.sendActionBar(
            "<second>Layer <gray>Has procced and broke <second>${blockCount.regex()} <gray>blocks".parse(true)
        )

        return EnchantResult(moneyAmount, coinsAmount, (blockCount / 4), blockCount)
    }

    private fun getMinAndMaxLocations(loc1: Location, loc2: Location): Pair<Location, Location> {
        require(loc1.world == loc2.world) { "Locations must be in the same world" }

        val minX = loc1.x.coerceAtMost(loc2.x)
        val minY = loc1.y.coerceAtMost(loc2.y)
        val minZ = loc1.z.coerceAtMost(loc2.z)

        val maxX = loc1.x.coerceAtLeast(loc2.x)
        val maxY = loc1.y.coerceAtLeast(loc2.y)
        val maxZ = loc1.z.coerceAtLeast(loc2.z)

        return Pair(Location(loc1.world, minX, minY, minZ), Location(loc1.world, maxX, maxY, maxZ))
    }
}