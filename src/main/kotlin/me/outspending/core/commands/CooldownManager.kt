package me.outspending.core.commands

import org.bukkit.entity.Player

class CooldownManager {

    private val cooldowns = mutableMapOf<String, Long>()

    fun isOnCooldown(player: Player, cooldownId: String): Boolean {
        val uuid = player.uniqueId.toString()
        val id = "$uuid:$cooldownId"
        val cooldown = cooldowns[id] ?: return false
        return cooldown > System.currentTimeMillis()
    }

    fun setCooldown(player: Player, cooldownId: String, cooldown: Long) {
        val uuid = player.uniqueId.toString()
        val id = "$uuid:$cooldownId"
        cooldowns[id] = System.currentTimeMillis() + cooldown
    }

    fun timeLeft(player: Player, cooldownId: String): Long {
        val uuid = player.uniqueId.toString()
        val id = "$uuid:$cooldownId"
        val cooldown = cooldowns[id] ?: return 0
        return cooldown - System.currentTimeMillis()
    }
}