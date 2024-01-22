package me.outspending.core.enchants.events

import me.outspending.core.utils.Utilities.Companion.getData
import org.bukkit.Material
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.entity.TNTPrimed
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityExplodeEvent
import kotlin.random.Random

class ExplosionEvent : Listener {
    private val random: Random = Random.Default

    @EventHandler
    fun onExplosion(e: EntityExplodeEvent) {
        val entity = e.entity
        if (entity.type == EntityType.PRIMED_TNT) {
            val tnt = entity as TNTPrimed
            val source = tnt.source

            if (source is Player) {
                val blockCount = e.blockList().size
                for (block in e.blockList()) {
                    block.type = Material.AIR
                }

                e.blockList().clear()

                source.getData()?.let { data ->
                    val money = random.nextDouble((25.0 * (blockCount) + 1), (100.0 * (blockCount + 1)))
                    val gold = (money / 5).toInt()

                    data.balance += money
                    data.gold += gold
                    source.giveExp(blockCount)
                }
            }
        }
    }
}