package me.outspending.core.commands.admin

import com.azuyamat.maestro.bukkit.annotations.Command
import com.azuyamat.maestro.bukkit.annotations.SubCommand
import me.outspending.core.Utilities.getData
import me.outspending.core.misc.helpers.FormatHelper.Companion.parse
import me.outspending.core.storage.registries.CellRegistry
import me.outspending.core.storage.registries.PlayerRegistry
import org.bukkit.entity.Player

@Command(
    name = "save",
    permission = "core.admin.save",
    description = "Save all/single player data"
)
class SaveCommand {
    fun onCommand(player: Player) {
        val data = player.getData() ?: return run {
            player.sendMessage("<gray>You do not have data to save".parse(true))
        }
        val cellId = data.cellId ?: return run {
            player.sendMessage("<gray>You do not have a cell to save".parse(true))
        }
        val cell = CellRegistry.getCell(cellId) ?: return run {
            player.sendMessage("<gray>Cell with id <#7ee37b><u>$cellId</u><gray> does not exist".parse(true))
        }

        PlayerRegistry.updatePlayerData(player.uniqueId, data)
        CellRegistry.updateCell(cell)

        player.sendMessage("<gray>Your data has been saved".parse(true))
    }

    @SubCommand(
        name = "all",
        permission = "core.admin.save.all",
        description = "Save all player data"
    )
    fun all(player: Player) {
        CellRegistry.updateAllCells()
        PlayerRegistry.updateAllPlayerData()

        player.sendMessage("<gray>All player data has been saved".parse(true))
    }
}