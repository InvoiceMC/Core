package me.outspending.core.commands.impl.admin

import com.azuyamat.maestro.bukkit.annotations.Command
import me.outspending.core.utils.helpers.FormatHelper.Companion.parse
import org.bukkit.entity.Player

@Command(name = "test", permission = "core.test", description = "wyd here nerd")
class TestCommand {

    fun onCommand(player: Player, string: String, prefix: Boolean? = false) {
        player.sendMessage(string.parse(prefix ?: false))
    }
}
