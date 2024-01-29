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
