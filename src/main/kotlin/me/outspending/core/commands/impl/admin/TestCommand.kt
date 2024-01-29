package me.outspending.core.commands.impl.admin

import com.azuyamat.maestro.bukkit.annotations.Command
import me.outspending.core.utils.Utilities.getConnection
import me.outspending.core.utils.helpers.FormatHelper.Companion.parse
import net.minecraft.core.BlockPos
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket
import net.minecraft.world.level.block.Blocks
import org.bukkit.entity.Player
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
                    val location = player.location
                    val connection = player.getConnection()!!
                    for (x in -size..size) {
                        for (y in -size..size) {
                            for (z in -size..size) {
                                val blockPos = BlockPos((location.x + x).toInt(), (location.y + y).toInt(), (location.z + z).toInt())
                                val packet = ClientboundBlockUpdatePacket(blockPos, Blocks.COBBLESTONE.defaultBlockState())

                                connection.send(packet)
                            }
                        }
                    }
                }

                player.sendMessage("Done: <yellow>$time".parse(true))
            }
        }.start()
    }
}