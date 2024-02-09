package me.outspending.core.commands.cell

import com.azuyamat.maestro.bukkit.annotations.Command
import me.outspending.core.storage.data.CellBoundData
import me.outspending.core.storage.data.CellData
import org.bukkit.entity.Player

@Command(
    name = "cell",
    description = "Manage your <main>cell",
)
class CellCommand {

    fun onCommand(player: Player) {

        val cell = CellData(
            name = "Test",
            members = mutableListOf(
                player.uniqueId
            ),
            bound = CellBoundData(
                center = player.location,
                size = 10
            ),
            spawnLocation = player.location
        ) // TODO: Get cell from player data
    }
}