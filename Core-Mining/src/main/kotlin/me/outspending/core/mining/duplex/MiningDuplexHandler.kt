package me.outspending.core.mining.duplex

import io.netty.channel.ChannelDuplexHandler
import io.netty.channel.ChannelHandlerContext
import me.outspending.core.data.Extensions.getData
import me.outspending.core.helpers.FormatHelper.Companion.parse
import me.outspending.core.mining.MetaStorage
import me.outspending.core.mining.enchants.EnchantHandler
import me.outspending.core.mining.enchants.EnchantResult
import me.outspending.core.pmines.Extensions.getPmine
import me.outspending.core.pmines.Extensions.hasLocation
import me.outspending.core.pmines.Mine
import me.outspending.core.pmines.PrivateMine
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

    private lateinit var metaStorage: MetaStorage

    override fun channelRead(
        channelHandlerContext: ChannelHandlerContext,
        packet: Any?,
    ) {
        if (packet is ServerboundPlayerActionPacket) {
            if (packet.action == ServerboundPlayerActionPacket.Action.START_DESTROY_BLOCK) {
                val mainHand = player.inventory.itemInMainHand
                if (mainHand.type != Material.DIAMOND_PICKAXE) return

                if (!this::metaStorage.isInitialized) {
                    metaStorage = MetaStorage(player, mainHand.itemMeta)
                }

                val pmine: PrivateMine = player.getPmine()
                val mine: Mine = pmine.getMine()
                val region: BoundingBox = mine.getRegion()

                val pos: BlockPos = packet.pos
                val location = toLocation(pos)

                if (region.hasLocation(location.toVector())) {
                    connection.send(ClientboundBlockDestructionPacket(player.entityId, pos, -1))

                    mine.removeBlock(location)
                    PacketSync.syncBlock(pmine, location, airBlock)

                    prisonBreak(player, location, mainHand, pmine)
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
        mine: PrivateMine
    ) {
        // TODO: Add a list per block material and check, right now it doesn't check therefore all
        // blocks are "mine able"
        if ((mainHand.type != Material.DIAMOND_PICKAXE)) return

        // Grab the player's data
        val data = player.getData()

        // Runs the metaStorage which automatically does everything for you.
        metaStorage.run()

        // Execute all the enchants that the player has on their pickaxe
        val result: EnchantResult = EnchantHandler.executeAllEnchants(player, data, metaStorage, location, mine)

        // Some other things
        if (player.level >= (100 + (25 * data.prestige))) {
            player.sendActionBar(
                "<red>You are at the max level, use <dark_red>/ᴘʀᴇꜱᴛɪɢᴇ".parse(),
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
    }
}
