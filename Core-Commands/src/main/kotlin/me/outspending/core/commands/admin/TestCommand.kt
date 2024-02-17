package me.outspending.core.commands.admin

import com.azuyamat.maestro.bukkit.annotations.Catcher
import com.azuyamat.maestro.bukkit.annotations.Command
import me.outspending.core.helpers.FormatHelper.Companion.parse
import org.bukkit.entity.Player

@Command(name = "test", permission = "core.test", description = "wyd here nerd")
class TestCommand {

    fun onCommand(player: Player, @Catcher message: String) {
        player.sendMessage(message.parse(true))
    }
}
