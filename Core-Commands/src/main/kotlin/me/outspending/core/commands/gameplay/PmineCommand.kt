package me.outspending.core.commands.gameplay

import com.azuyamat.maestro.bukkit.annotations.Command
import com.azuyamat.maestro.bukkit.annotations.SubCommand
import me.outspending.core.pmines.data.pmineDataManager
import me.outspending.core.misc.WeightedCollection
import me.outspending.core.pmines.Mine
import me.outspending.core.pmines.PrivateMine
import org.bukkit.Material
import org.bukkit.block.data.BlockData
import org.bukkit.entity.Player

@Command(
    name = "pmine",
    description = "PMINES!?!?!?",
    aliases = ["p", "pm"]
)
class PmineCommand {

    fun onCommand(player: Player) {
        player.sendMessage("Pmines!")
    }

    @SubCommand(
        name = "create",
        description = "Create a pmine"
    )
    fun createPmine(player: Player, name: String) {
        val plrLocation = player.location
        val loc1 = plrLocation.clone().add(5.0, 5.0, 5.0)
        val loc2 = plrLocation.clone().subtract(5.0, 5.0, 5.0)
        val collection = WeightedCollection<BlockData>()
            .add(75.0, Material.STONE.createBlockData())
            .add(25.0, Material.COBBLESTONE.createBlockData())

        val mine = Mine(loc1, loc2, collection)
        val pmine = PrivateMine.createMine(player, name, player.location, mine) ?: return
        pmineDataManager.createPmineData(player.uniqueId.toString(), pmine)

    }
}