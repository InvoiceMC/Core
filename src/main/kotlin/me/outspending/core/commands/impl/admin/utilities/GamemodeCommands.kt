package me.outspending.core.commands.impl.admin.utilities

import com.azuyamat.maestro.bukkit.annotations.Command
import me.outspending.core.utils.helpers.FormatHelper.Companion.parse
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
        executor.sendMessage("<gray>Your gamemode has been set to <main>${gamemode.name.lowercase()}".parse(true))
    } else {
        executor.sendMessage("<main>${target.name.lowercase()}<main>'s <gray>gamemode has been set to <main>${gamemode.name.lowercase()}".parse(true))
        target.sendMessage("<main>${executor.name.lowercase()} <gray>has set your gamemode to <main>${gamemode.name.lowercase()}".parse(true))
    }
}