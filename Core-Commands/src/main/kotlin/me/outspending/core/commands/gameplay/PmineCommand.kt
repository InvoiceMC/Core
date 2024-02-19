package me.outspending.core.commands.gameplay

import com.azuyamat.maestro.bukkit.annotations.Command
import com.azuyamat.maestro.bukkit.annotations.SubCommand
import me.outspending.core.data.Extensions.getData
import me.outspending.core.helpers.FormatHelper.Companion.parse
import me.outspending.core.pmines.data.pmineDataManager
import me.outspending.core.misc.WeightedCollection
import me.outspending.core.pmines.Extensions.getPmine
import me.outspending.core.pmines.Extensions.hasPmine
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

    private fun checkPmine(player: Player): Boolean {
        if (!player.hasPmine()) {
            player.sendMessage("<red>You don't have a pmine!".parse(true))
            return false
        }
        return true
    }

    fun onCommand(player: Player) {
        player.sendMessage("Pmines!")
    }

    @SubCommand(
        name = "create",
        description = "Create a pmine"
    )
    fun createPmine(player: Player, name: String) {
        if (checkPmine(player)) return

        if (name.length > 12) {
            player.sendMessage("<red>The name of the pmine can't be longer than 12 characters!".parse(true))
            return
        }

        if (pmineDataManager.hasPmineName(name)) {
            player.sendMessage("<red>A pmine with that name already exists!".parse(true))
            return
        }

        val privateMine = PrivateMine.createMine(name, player)
        pmineDataManager.savePmine(privateMine)
    }

    @SubCommand(
        name = "info",
        description = "Get info about your pmine"
    )
    fun pmineInfo(player: Player) {
        if (!checkPmine(player)) return

        val pmine = player.getPmine()
        pmine.showInfo(player)
    }
}