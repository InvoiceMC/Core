package me.outspending.core.commands.impl.admin.utilities

import com.azuyamat.maestro.bukkit.annotations.Command
import me.outspending.core.core
import org.bukkit.GameMode
import org.bukkit.entity.Player

@Command(
    name = "gmc",
    description = "Set your gamemode to <main>creative",
    permission = "core.admin.utilities.gamemode"
)
class GamemodeCreativeCommand {
    fun onCommand(player: Player, target: Player? = null) {
        setGameMode(player, GameMode.CREATIVE, target)
    }
}

@Command(
    name = "gms",
    description = "Set your gamemode to <main>survival",
    permission = "core.admin.utilities.gamemode"
)
class GamemodeSurvivalCommand {
    fun onCommand(player: Player, target: Player? = null) {
        setGameMode(player, GameMode.SURVIVAL, target)
    }
}

@Command(
    name = "gma",
    description = "Set your gamemode to <main>adventure",
    permission = "core.admin.utilities.gamemode"
)
class GamemodeAdventureCommand {
    fun onCommand(player: Player, target: Player? = null) {
        setGameMode(player, GameMode.ADVENTURE, target)
    }
}

@Command(
    name = "gmsp",
    description = "Set your gamemode to <main>spectator",
    permission = "core.admin.utilities.gamemode"
)
class GamemodeSpectatorCommand {
    fun onCommand(player: Player, target: Player? = null) {
        setGameMode(player, GameMode.SPECTATOR, target)
    }
}

fun setGameMode(executor: Player, gamemode: GameMode, target: Player? = null) {
    val target = target ?: executor
    target.gameMode = gamemode

    if (executor == target) {
        val message = core.messageConfig.getMessageWithArgs(
            "commands.admin.utilities.gamemode.success",
            true,
            gamemode.name.lowercase()
        )
        executor.sendMessage(message)
    } else {
        val message = core.messageConfig.getMessageWithArgs(
            "commands.utilities.gamemode.success_other",
            true,
            target.name.lowercase(),
            gamemode.name.lowercase()
        )
        executor.sendMessage(message)
        val receiverMessage = core.messageConfig.getMessageWithArgs(
            "commands.utilities.gamemode.success_other_receiver",
            true,
            executor.name.lowercase(),
            gamemode.name.lowercase()
        )
        target.sendMessage(receiverMessage)
    }
}