package me.outspending.core.packets.listener

import io.netty.channel.ChannelDuplexHandler
import io.netty.channel.ChannelHandlerContext
import me.outspending.core.utils.Utilities.getConnection
import net.minecraft.core.BlockPos
import net.minecraft.network.protocol.game.ClientboundBlockDestructionPacket
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket
import net.minecraft.network.protocol.game.ServerboundUseItemOnPacket
import net.minecraft.server.network.ServerGamePacketListenerImpl
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Player

class MiningDuplexHandler(private val player: Player, private val connection: ServerGamePacketListenerImpl) : ChannelDuplexHandler() {

    override fun channelRead(channelHandlerContext: ChannelHandlerContext, packet: Any?) {
        if (packet is ServerboundPlayerActionPacket) {
            if (packet.action != ServerboundPlayerActionPacket.Action.START_DESTROY_BLOCK) {
                return;
            }

            val pos: BlockPos = packet.pos
            val location = Location(player.world, pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble())
            val packet = ClientboundBlockDestructionPacket(player.entityId, pos, -1)
            sendBreakPacket(connection, packet)

            player.sendBlockChange(location, Material.AIR.createBlockData())
            return;
        } else if (packet is ServerboundUseItemOnPacket) {
            return;
        }

        super.channelRead(channelHandlerContext, packet)
    }

    private fun sendBreakPacket(connection: ServerGamePacketListenerImpl, packet: ClientboundBlockDestructionPacket) {
        try {
            val field = packet.javaClass.getDeclaredField("a")
            field.isAccessible = true
            field.setInt(packet, 123)
            field.isAccessible = false
        } catch (e: Exception) {
            e.printStackTrace()
        }

        connection.send(packet)
    }
}