package me.outspending.core.listeners

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent

class MiscListeners : Listener {

    @EventHandler fun onDamage(e: EntityDamageEvent) = run { e.isCancelled = true }
}
