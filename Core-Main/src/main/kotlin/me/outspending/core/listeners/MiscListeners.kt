package me.outspending.core.listeners

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.FoodLevelChangeEvent

class MiscListeners : Listener {

    @EventHandler fun onDamage(e: EntityDamageEvent) = run { e.isCancelled = true }
    @EventHandler fun onFoodLevelChange(e: FoodLevelChangeEvent) = run { e.isCancelled = true }
}
