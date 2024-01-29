package me.outspending.core.enchants.types

import com.sk89q.worldedit.EditSession
import com.sk89q.worldedit.WorldEdit
import com.sk89q.worldedit.bukkit.BukkitAdapter
import com.sk89q.worldedit.function.mask.BlockMask
import com.sk89q.worldedit.regions.CuboidRegion
import com.sk89q.worldedit.regions.Region
import com.sk89q.worldedit.world.block.BaseBlock
import com.sk89q.worldedit.world.block.BlockTypes
import me.outspending.core.enchants.EnchantResult
import me.outspending.core.enchants.PickaxeEnchant
import me.outspending.core.storage.data.PlayerData
import me.outspending.core.utils.Utilities.regex
import me.outspending.core.utils.Utilities.toComponent
import net.kyori.adventure.title.Title
import net.minecraft.core.BlockPos
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket
import net.minecraft.server.network.ServerGamePacketListenerImpl
import net.minecraft.world.level.block.Blocks
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.persistence.PersistentDataContainer
import kotlin.random.Random
import kotlin.time.measureTime

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

        val loc1 = blockLocation.clone().add(5.0, 5.0, 5.0)
        val loc2 = blockLocation.clone().add(-5.0, -5.0, -5.0)
        val blockCount = getBlockCount(loc1, loc2).blockCount

        val time = measureTime {
            for (x in -5..5) {
                for (z in -5..5) {
                    val blockPos = BlockPos((blockLocation.blockX + x), blockLocation.blockY, (blockLocation.blockZ + z))
                    val packet = ClientboundBlockUpdatePacket(blockPos, Blocks.AIR.defaultBlockState())

                    playerConnection.send(packet)
                }
            }
        }

        player.sendMessage("<yellow>$time".toComponent())

        val moneyAmount: Double = random.nextDouble(10.0, 25.0) * blockCount
        val coinsAmount: Int = (moneyAmount / 5).toInt()

        player.showTitle(
            Title.title(
                "<gradient:#e08a19:#e8b36d><b>JACKHAMMER".toComponent(),
                "<gray>Blocks Broken: <#e8b36d>${blockCount.regex()}".toComponent()
            )
        )

        return EnchantResult(moneyAmount, coinsAmount, blockCount)
    }
}
