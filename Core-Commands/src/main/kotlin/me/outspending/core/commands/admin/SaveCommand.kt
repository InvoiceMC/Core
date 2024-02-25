package me.outspending.core.commands.admin

import com.azuyamat.maestro.bukkit.annotations.Command
import com.azuyamat.maestro.bukkit.annotations.SubCommand
import me.outspending.core.data.DataSaver
import me.outspending.core.data.Extensions.savePlayerData
import me.outspending.core.helpers.FormatHelper.Companion.parse
import org.bukkit.entity.Player

@Command(
    name = "save",
    permission = "core.admin.save",
    description = "Save all/single player data"
)
class SaveCommand {
    fun onCommand(player: Player) {
//        val cellId = data.cellId ?: return run {
//            player.sendMessage("<gray>You do not have a cell to save".parse(true))
//        }
//        val cell = CellRegistry.getCell(cellId) ?: return run {
//            player.sendMessage("<gray>Cell with id <#7ee37b><u>$cellId</u><gray> does not exist".parse(true))
//        }

        player.savePlayerData()
        // CellRegistry.updateCell(cell)

        player.sendMessage("<gray>Your data has been saved".parse(true))
    }

    @SubCommand(
        name = "all",
        permission = "core.admin.save.all",
        description = "Save all player data"
    )
    fun all(player: Player) {
        DataSaver.updateAllData()

        player.sendMessage("<gray>All player data has been saved".parse(true))
    }
}