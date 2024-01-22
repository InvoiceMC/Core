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
import me.outspending.core.storage.PlayerData
import me.outspending.core.utils.Utilities.Companion.regex
import me.outspending.core.utils.Utilities.Companion.toComponent
import net.kyori.adventure.title.Title
import org.bukkit.Location
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.persistence.PersistentDataType
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
        dataContainer: PersistentDataContainer,
        enchantmentLevel: Int,
        blockLocation: Location,
        random: Random
    ): EnchantResult {
        if (random.nextDouble() > getEnchantmentChance(enchantmentLevel)) return EnchantResult()

        val vector1 = BukkitAdapter.asBlockVector(blockLocation.clone().add(5.0, 0.0, 5.0))
        val vector2 = BukkitAdapter.asBlockVector(blockLocation.clone().add(-5.0, 0.0, -5.0))

        val editSession: EditSession =
            WorldEdit.getInstance().newEditSession(BukkitAdapter.adapt(player.world))
        val mask =
            BlockMask(
                editSession,
                BaseBlock(BlockTypes.COBBLESTONE?.defaultState),
                BaseBlock(BlockTypes.STONE?.defaultState)
            )
        val region: Region = CuboidRegion(vector1, vector2)

        editSession.mask = mask

        val blockCount = editSession.countBlocks(region, mask)
        editSession.setBlocks(region, BlockTypes.AIR)
        editSession.flushQueue()

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
