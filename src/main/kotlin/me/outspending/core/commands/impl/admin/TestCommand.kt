package me.outspending.core.commands.impl.admin

import me.outspending.core.commands.annotations.Catcher
import me.outspending.core.commands.annotations.Command
import me.outspending.core.commands.annotations.SubCommand
import me.outspending.core.utils.helpers.FormatHelper.Companion.parse
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.event.HoverEvent
import org.bukkit.entity.Player

@Command(
    name = "test",
    permission = "core.test",
    description = "wyd here nerd"
)
class TestCommand {

    fun onCommand(player: Player) {
        player.sendMessage("Welcome to the test command".parse(true))
    }

    @SubCommand("format")
    fun format(player: Player, @Catcher message: String) {
        player.sendMessage(
            message.parse(true)
                .clickEvent(ClickEvent.copyToClipboard(message))
                .hoverEvent(HoverEvent.showText("<gray>Click to copy".parse()))
        )
    }
}