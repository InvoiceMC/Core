package me.outspending.core.commands.admin

import com.azuyamat.maestro.bukkit.annotations.Command
import me.outspending.core.Utilities.format
import me.outspending.core.helpers.FormatHelper.Companion.parse
import me.outspending.core.mining.MineUtils
import me.outspending.core.mining.shapes.CuboidShape
import me.outspending.core.misc.WeightedCollection
import org.bukkit.Material
import org.bukkit.block.data.BlockData
import org.bukkit.entity.Player
import org.bukkit.util.Vector
import kotlin.time.measureTime

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

                        val collection =
                            WeightedCollection<BlockData>()
                                .add(0.25, Material.COBBLESTONE.createBlockData())
                                .add(0.25, Material.STONE.createBlockData())
                                .add(0.25, Material.ANDESITE.createBlockData())
                                .add(0.25, Material.DIORITE.createBlockData())

                        val shape = CuboidShape(vec1, vec2)

                        val amount =
                            MineUtils.setBlocks(player, player.location, shape, collection, true)
                        player.sendMessage("Changed: <second>${amount.format()}".parse(true))
                    }

                    player.sendMessage("Done: <second>$time".parse(true))
                }
            }
            .start()
    }
}
