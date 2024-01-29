package me.outspending.core.commands.impl.admin

import com.azuyamat.maestro.bukkit.annotations.Command
import me.outspending.core.utils.MineUtils
import me.outspending.core.utils.Utilities.getConnection
import me.outspending.core.utils.helpers.FormatHelper.Companion.parse
import net.minecraft.core.BlockPos
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket
import net.minecraft.world.level.block.Blocks
import org.bukkit.Material
import org.bukkit.block.data.BlockData
import org.bukkit.entity.Player
import org.bukkit.util.Vector
import kotlin.time.measureTime

@Command(
    name = "test",
    permission = "core.test",
    description = "wyd here nerd"
)
class TestCommand {

    fun onCommand(player: Player, size: Int) {
        object : Thread() {
            override fun run() {
                val time = measureTime {
                    val vec1 = Vector(-size, -size, -size)
                    val vec2 = Vector(size, size, size)
                    val blockData: BlockData = Material.COBBLESTONE.createBlockData()

                    MineUtils.setBlocksXYZ(player, player.location, vec1, vec2, blockData, true)
                }

                player.sendMessage("Done: <yellow>$time".parse(true))
            }
        }.start()
    }
}