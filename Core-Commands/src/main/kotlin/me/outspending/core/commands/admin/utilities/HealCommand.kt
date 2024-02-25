package me.outspending.core.commands.admin.utilities

import com.azuyamat.maestro.common.annotations.Command
import me.outspending.core.config.messagesConfig
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Player

const val MAX_FOOD_LEVEL = 20

@Command(
    name = "heal",
    description = "Heal a player or yourself",
    permission = "core.admin.heal"
)
class HealCommand {
    fun onCommand(player: Player, target: Player?) {
        val target = target ?: player
        val maxHealth = target.getAttribute(Attribute.GENERIC_MAX_HEALTH)?.value ?: 20.0

        target.health = maxHealth
        target.foodLevel = MAX_FOOD_LEVEL
        target.sendMessage(
            messagesConfig.getMessageOr(
                "commands.admin.heal_success",
                true,
                "<gray>You have been healed!"
            )
        )
    }
}