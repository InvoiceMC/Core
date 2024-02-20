package me.outspending.core.mining.duplex

import io.netty.channel.ChannelDuplexHandler
import io.netty.channel.ChannelHandlerContext
import me.outspending.core.Utilities.toComponent
import me.outspending.core.data.Extensions.getData
import me.outspending.core.mining.PickaxeUpdater
import me.outspending.core.mining.enchants.EnchantHandler
import me.outspending.core.mining.enchants.EnchantResult
import me.outspending.core.pmines.Extensions.getPmine
import me.outspending.core.pmines.Mine
import me.outspending.core.pmines.sync.PacketSync
import net.minecraft.core.BlockPos
import net.minecraft.network.protocol.game.ClientboundBlockDestructionPacket
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket
import net.minecraft.network.protocol.game.ServerboundUseItemOnPacket
import net.minecraft.server.network.ServerGamePacketListenerImpl
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.util.BoundingBox
import kotlin.random.Random

class MiningDuplexHandler(
    private val player: Player,
    private val connection: ServerGamePacketListenerImpl,
) : ChannelDuplexHandler() {
    private val random: Random = Random.Default
    private val airBlock = Material.AIR.createBlockData()

    override fun channelRead(
        channelHandlerContext: ChannelHandlerContext,
        packet: Any?,
    ) {
        if (packet is ServerboundPlayerActionPacket) {
            if (packet.action == ServerboundPlayerActionPacket.Action.START_DESTROY_BLOCK) {
                val mainHand = player.inventory.itemInMainHand
                if (mainHand.type == Material.DIAMOND_PICKAXE) {
                    val pmine = player.getPmine()
                    val mine = pmine.getMine()
                    val region = mine.getRegion()

                    val pos: BlockPos = packet.pos
                    val location = toLocation(pos)

                    if (region.contains(location.toVector())) {
                        connection.send(ClientboundBlockDestructionPacket(player.entityId, pos, -1))
                        PacketSync.syncBlock(pmine, location, airBlock)

                        prisonBreak(player, location, mainHand, region)
                    }
                }
            }
        } else if (packet is ServerboundUseItemOnPacket) {
            return
        }

        super.channelRead(channelHandlerContext, packet)
    }

    /** Converts a BlockPos to a Location */
    private fun toLocation(pos: BlockPos): Location = Location(player.world, pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble())

    /** Used for all the enchants in Invoice */
    private fun prisonBreak(
        player: Player,
        location: Location,
        mainHand: ItemStack,
        region: BoundingBox
    ) {
        // TODO: Add a list per block material and check, right now it doesn't check therefore all
        // blocks are "mine able"
        if ((mainHand.type != Material.DIAMOND_PICKAXE)) return

        // Grab the player's data
        val data = player.getData()

        // Execute all the enchants that the player has on their pickaxe
        val result: EnchantResult = EnchantHandler.executeAllEnchants(player, data, location, region, random)

        // Some other things
        if (player.level >= (100 + (25 * data.prestige))) {
            player.sendActionBar(
                "<red>You are at the max level, use <dark_red>/ᴘʀᴇꜱᴛɪɢᴇ".toComponent(),
            )
        } else {
            player.giveExp(1 + result.xp)
        }

        val blockMoney = random.nextDouble(5.0, 15.0)
        val blockGold = blockMoney / 5

        // Add to the player's data
        data.gold += ((blockGold + result.gold) * data.multiplier).toInt()
        data.balance += ((blockMoney + result.money) * data.multiplier)
        data.blocksBroken += 1

        val newItem = PickaxeUpdater.updatePickaxe(mainHand, result.blocks)
        player.inventory.setItemInMainHand(newItem)
    }
}
