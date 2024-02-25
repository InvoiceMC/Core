package me.outspending.core.commands.admin.utilities

import com.azuyamat.maestro.common.annotations.Command
import com.azuyamat.maestro.common.annotations.SubCommand
import me.outspending.core.config.messagesConfig
import me.outspending.core.helpers.NumberHelper
import org.bukkit.entity.Player

@Command(
    name = "speed",
    description = "Set your <main>speed",
    permission = "core.admin.utilities.speed"
)
class SpeedCommand {
    @SubCommand(
        name = "walk",
        description = "Set your <main>walk speed",
        permission = "core.admin.utilities.speed"
    )
    fun walk(player: Player, speed: Float) {
        val parsedSpeed = NumberHelper(speed).clamp(-1.0f, 1.0f).toFloat()
        player.walkSpeed = parsedSpeed
        val message = messagesConfig.getMessageWithArgs(
            "commands.admin.utilities.speed.walk_success",
            true,
            parsedSpeed
        )
        player.sendMessage(message)
    }

    @SubCommand(
        name = "fly",
        description = "Set your <main>fly speed",
        permission = "core.admin.utilities.speed"
    )
    fun fly(player: Player, speed: Float) {
        val parsedSpeed = NumberHelper(speed).clamp(-1.0f, 1.0f).toFloat()
        player.flySpeed = parsedSpeed
        val message = messagesConfig.getMessageWithArgs(
            "commands.admin.utilities.speed.fly_success",
            true,
            parsedSpeed
        )
        player.sendMessage(message)
    }

    @SubCommand(
        name = "reset",
        description = "Reset your <main>speed",
        permission = "core.admin.utilities.speed"
    )
    fun reset(player: Player) {
        player.walkSpeed = 0.2f
        player.flySpeed = 0.1f
        player.sendMessage(messagesConfig.getMessage("commands.admin.utilities.speed.reset_success"))
    }
}