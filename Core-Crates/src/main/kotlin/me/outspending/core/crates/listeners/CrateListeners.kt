package me.outspending.core.crates.listeners

import de.tr7zw.changeme.nbtapi.NBTBlock
import me.outspending.core.crates.cratesHandler
import me.outspending.core.helpers.FormatHelper.Companion.parse
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.player.PlayerInteractEvent

class CrateListeners : Listener {

    @EventHandler
    fun onCrateClick(e: PlayerInteractEvent) {
        if (e.clickedBlock == null) return
        if (e.hand == org.bukkit.inventory.EquipmentSlot.OFF_HAND) return
        val crateID = NBTBlock(e.clickedBlock).data.getString("crate") ?: return
        val crate = cratesHandler.getCrate(crateID) ?: return
        e.isCancelled = true
        if (e.action == Action.LEFT_CLICK_BLOCK || e.action == Action.LEFT_CLICK_AIR) {
            // open rewards
            return
        }
        if (e.player.inventory.itemInMainHand.isSimilar(crate.getItemKey())) {
            crate.openCrate(e.player, e.player.isSneaking)
        } else {
            e.player.sendMessage("<red>You need to hold the key to open this crate!".parse(true))
            e.player.inventory.addItem(crate.getItemKey())
        }
    }

    @EventHandler
    fun onCrateBreak(e: BlockBreakEvent) {
        val crateID = NBTBlock(e.block).data.getString("crate") ?: return
        val crate = cratesHandler.getCrate(crateID) ?: return
        e.isCancelled = true
    }

    @EventHandler
    fun onCrateBelowBreak(e: BlockBreakEvent) {
        val crateID = NBTBlock(e.block.location.clone().add(0.0, 1.0, 0.0).block).data.getString("crate") ?: return
        val crate = cratesHandler.getCrate(crateID) ?: return
        e.isCancelled = true
    }

    @EventHandler
    fun onCrateBelowClick(e: PlayerInteractEvent) {
        if (e.clickedBlock == null) return
        val crateID = NBTBlock(e.clickedBlock!!.location.clone().add(0.0, 1.0, 0.0).block).data.getString("crate") ?: return
        val crate = cratesHandler.getCrate(crateID) ?: return
        e.isCancelled = true
    }

}