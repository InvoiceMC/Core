package me.outspending.core.commands.gameplay

import com.azuyamat.maestro.bukkit.annotations.Command
import org.bukkit.entity.Player

@Command(
    name = "quests",
    description = "View your current quests",
    permission = "core.quests.view"
)
class QuestsCommand {
    fun onCommand(player: Player) {
        player.sendMessage("Opening quests GUI")
        // QuestsGUI(player).open()
    }
}