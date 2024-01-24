package me.outspending.core.mine

import me.outspending.core.enchants.gui.EnchantGUI
import me.outspending.core.enchants.EnchantHandler
import me.outspending.core.enchants.EnchantResult
import me.outspending.core.utils.Utilities.getData
import me.outspending.core.utils.Utilities.toComponent
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot
import kotlin.random.Random

class MineListener : Listener {
    companion object {
        val MINE_BLOCKS: List<Material> =
            listOf(
                Material.STONE,
                Material.COBBLESTONE,
            )

        private val RANDOM: Random = Random.Default
    }

    @EventHandler
    fun onBlockBreak(e: BlockBreakEvent) {
        val player: Player = e.player
        val block: Block = e.block
        val mainHand = player.inventory.itemInMainHand

        if ((mainHand.type != Material.DIAMOND_PICKAXE) or (!MINE_BLOCKS.contains(block.type)))
            return

        // Check if the player has data and if it is, keep executing the code
        player.getData()?.let { data ->
            // Execute all the enchants that the player has on their pickaxe
            val result: EnchantResult =
                EnchantHandler.executeAllEnchants(player, data, block.location, RANDOM)

            // Some other things
            e.isDropItems = false
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

    @EventHandler
    fun onPickaxeRightClick(e: PlayerInteractEvent) {
        if (e.hand != EquipmentSlot.HAND) return
        val player = e.player
        val action = e.action

        // Checks
        if (!player.isSneaking) return
        if (action == Action.LEFT_CLICK_BLOCK || action == Action.LEFT_CLICK_AIR) return

        // Then check if the player is holding a pickaxe and open the GUI
        if (player.inventory.itemInMainHand.type == Material.DIAMOND_PICKAXE) {
            EnchantGUI(player).openGUI()
        }
    }
}
