package me.outspending.core.commands.gameplay

import com.azuyamat.maestro.bukkit.annotations.Command
import me.outspending.core.quests.guis.QuestsGUI
import org.bukkit.entity.Player

@Command(
    name = "quests",
    description = "View your current quests",
    permission = "core.quests.view"
)
class QuestsCommand {
    fun onCommand(player: Player) {
        QuestsGUI(player).open()
    }
}