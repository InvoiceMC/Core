package me.outspending.core.mining.pickaxe

import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot

class PickaxeListener : Listener {
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
            // TODO: Open the GUI
            player.sendMessage("You right clicked with a pickaxe")
        }
    }
}
