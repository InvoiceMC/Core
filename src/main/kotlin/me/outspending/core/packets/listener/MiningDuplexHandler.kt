package me.outspending.core.packets.listener

import io.netty.channel.ChannelDuplexHandler
import io.netty.channel.ChannelHandlerContext
import me.outspending.core.enchants.EnchantHandler
import me.outspending.core.enchants.EnchantResult
import me.outspending.core.packets.sync.PacketSync
import me.outspending.core.utils.MineUtils
import me.outspending.core.utils.Utilities.getData
import me.outspending.core.utils.Utilities.toComponent
import net.minecraft.core.BlockPos
import net.minecraft.network.protocol.game.ClientboundBlockDestructionPacket
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket
import net.minecraft.network.protocol.game.ServerboundUseItemOnPacket
import net.minecraft.server.network.ServerGamePacketListenerImpl
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import kotlin.random.Random

class MiningDuplexHandler(
    private val player: Player,
    private val connection: ServerGamePacketListenerImpl
) : ChannelDuplexHandler() {
    private val RANDOM: Random = Random.Default
    private val NULLBLOCK = Material.AIR.createBlockData()

    override fun channelRead(channelHandlerContext: ChannelHandlerContext, packet: Any?) {
        if (packet is ServerboundPlayerActionPacket) {
            if (packet.action != ServerboundPlayerActionPacket.Action.START_DESTROY_BLOCK) {
                return
            }

            val mainHand = player.inventory.itemInMainHand
            if (mainHand.type != Material.DIAMOND_PICKAXE) {
                return
            }

            val pos: BlockPos = packet.pos
            val location = toLocation(pos)

            connection.send(ClientboundBlockDestructionPacket(player.entityId, pos, -1))
            PacketSync.syncBlock(location, NULLBLOCK)

            prisonBreak(player, location, mainHand)
            return
        } else if (packet is ServerboundUseItemOnPacket) {
            return
        }

        super.channelRead(channelHandlerContext, packet)
    }


    /** Converts a BlockPos to a Location */
    private fun toLocation(pos: BlockPos): Location =
        Location(player.world, pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble())

    /** Used for all the enchants in Invoice */
    private fun prisonBreak(player: Player, location: Location, mainHand: ItemStack) {
        // TODO: Add a list per block material and check, right now it doesn't check therefore all
        // blocks are "mineable"
        if ((mainHand.type != Material.DIAMOND_PICKAXE)) return

        // Check if the player has data and if it is, keep executing the code
        player.getData()?.let { data ->
            // Execute all the enchants that the player has on their pickaxe
            val result: EnchantResult =
                EnchantHandler.executeAllEnchants(player, data, location, RANDOM)

            // Some other things
            if (player.level >= (100 + (25 * data.prestige))) {
                player.sendActionBar(
                    "<red>You are at the max level, use <dark_red>/ᴘʀᴇꜱᴛɪɢᴇ".toComponent()
                )
            } else {
                player.giveExp(1 + result.xp)
            }

            val blockMoney = RANDOM.nextDouble(5.0, 15.0)
            val blockGold = blockMoney / 5

            // Add to the player's data
            data.gold += ((blockGold + result.gold) * data.multiplier).toInt()
            data.balance += ((blockMoney + result.money) * data.multiplier)
            data.blocksBroken += 1
        }
    }
}
