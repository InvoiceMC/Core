package me.outspending.core.commands.impl.admin

import com.azuyamat.maestro.bukkit.annotations.Command
import me.outspending.core.utils.Utilities.getConnection
import net.minecraft.core.BlockPos
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket
import net.minecraft.world.level.block.Blocks
import org.bukkit.entity.Player

@Command(
    name = "test",
    permission = "core.test",
    description = "wyd here nerd"
)
class TestCommand {

    fun onCommand(player: Player) {
        val connection = player.getConnection()!!
        for (x in -10..10) {
            for (y in -10..10) {
                for (z in -10..10) {
                    val blockPos = BlockPos(x, y, z)
                    val packet = ClientboundBlockUpdatePacket(blockPos, Blocks.COBBLESTONE.defaultBlockState())

                    connection.send(packet)
                }
            }
        }
    }
}