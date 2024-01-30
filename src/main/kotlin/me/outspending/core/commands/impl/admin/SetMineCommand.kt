package me.outspending.core.commands.impl.admin

import com.azuyamat.maestro.bukkit.annotations.Command
import kotlin.time.measureTime
import me.outspending.core.utils.MineUtils
import me.outspending.core.utils.helpers.FormatHelper.Companion.parse
import me.outspending.core.utils.shapes.CuboidShape
import org.bukkit.Material
import org.bukkit.block.data.BlockData
import org.bukkit.entity.Player
import org.bukkit.util.Vector

@Command(
    name = "setmine",
    permission = "core.setmine",
    description = "Set a mine",
)
class SetMineCommand {

    fun onCommand(player: Player, size: Int) {
        object : Thread() {
                override fun run() {
                    val time = measureTime {
                        val vec1 = Vector(-size, -size, -size)
                        val vec2 = Vector(size, size, size)

                        val blockData: BlockData = Material.COBBLESTONE.createBlockData()
                        val shape = CuboidShape(vec1, vec2)

                        MineUtils.setBlocks(player, player.location, shape, blockData, true)
                    }

                    player.sendMessage("Done: <yellow>$time".parse(true))
                }
            }
            .start()
    }
}