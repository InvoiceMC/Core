package me.outspending.core.commands.impl.admin.utilities

import com.azuyamat.maestro.bukkit.annotations.Command
import com.azuyamat.maestro.bukkit.annotations.SubCommand
import me.outspending.core.messageConfig
import me.outspending.core.utils.helpers.FormatHelper.Companion.parse
import me.outspending.core.utils.helpers.NumberHelper
import org.bukkit.entity.Player

@Command(
    name  = "speed",
    description = "Set your <main>speed",
    permission = "core.admin.utilities.speed"
)
class SpeedCommand {

    fun onCommand(player: Player) {
        player.sendMessage(messageConfig.getValue("commands.admin.utilities.speed.main").asFormattedMessage(true))
    }

    @SubCommand(
        name = "walk",
        description = "Set your <main>walk speed",
        permission = "core.admin.utilities.speed"
    )
    fun walk(player: Player, speed: Float) {
        val parsedSpeed = NumberHelper(speed).clamp(-1.0f, 1.0f).toFloat()
        player.walkSpeed = parsedSpeed
        val message = messageConfig.getValue("commands.admin.utilities.speed.walk_success")
            .parseArgs(parsedSpeed)
        player.sendMessage(message.parse(true))
    }

    @SubCommand(
        name = "fly",
        description = "Set your <main>fly speed",
        permission = "core.admin.utilities.speed"
    )
    fun fly(player: Player, speed: Float) {
        val parsedSpeed = NumberHelper(speed).clamp(-1.0f, 1.0f).toFloat()
        player.flySpeed = parsedSpeed
        val message = messageConfig.getValue("commands.admin.utilities.speed.fly_success")
            .parseArgs(parsedSpeed)
        player.sendMessage(message.parse(true))
    }

    @SubCommand(
        name = "reset",
        description = "Reset your <main>speed",
        permission = "core.admin.utilities.speed"
    )
    fun reset(player: Player) {
        player.walkSpeed = 0.2f
        player.flySpeed = 0.1f
        player.sendMessage(messageConfig.getValue("commands.admin.utilities.speed.reset_success").asFormattedMessage(true))
    }
}