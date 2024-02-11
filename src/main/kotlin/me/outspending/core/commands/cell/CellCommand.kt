package me.outspending.core.commands.cell

import com.azuyamat.maestro.bukkit.annotations.Command
import com.azuyamat.maestro.bukkit.annotations.SubCommand
import me.outspending.core.Utilities.getData
import me.outspending.core.core
import me.outspending.core.misc.helpers.FormatHelper.Companion.parse
import me.outspending.core.storage.data.CellData
import me.outspending.core.storage.registries.CellRegistry
import me.outspending.core.storage.registries.PlayerRegistry
import org.bukkit.entity.Player

@Command(
    name = "cell",
    description = "Manage your <main>cell",
)
class CellCommand {

    fun onCommand(player: Player) {
        val playerData = player.getData()
        val cellId = playerData?.cellId ?:
            return sendCellNotFoundMessage(player)
        val cell = CellRegistry.getCell(cellId) ?:
            return sendCellNotFoundMessage(player)

        player.sendMessage("<gray>Dope! You got a <main>cell.".parse(true))
    }

    @SubCommand(
        name = "create",
        description = "Create a new <main>cell",
        permission = "cell.create"
    )
    fun create(player: Player, name: String) {
        val playerData = player.getData() ?: return run {
            player.sendMessage("<gray>There was an error while getting your data".parse(true))
        }
        if (playerData?.cellId != null) {
            return sendCellAlreadyExistsMessage(player)
        }

        val cellExists = CellRegistry.cellExists(name)
        if (cellExists) {
            player.sendMessage("<gray>The cell name $name<main> <gray>is already taken".parse(true))
            return
        }

        val isValid = CellRegistry.isValidCellName(name)
        if (!isValid) {
            player.sendMessage("<gray>The cell name $name<main> <gray>is not valid".parse(true))
            return
        }

        val cell = CellData(name, player)
        CellRegistry.addCell(cell)
        playerData.cellId = name

        player.sendMessage("""
            <gray>Congratulations! You have created a new cell named <main>$name<gray>!
        """.trimIndent().parse(true))
    }

    private fun sendCellAlreadyExistsMessage(player: Player) {
        player.sendMessage(
            core.messageConfig.getMessageOr(
                "commands.cell.cell-exists",
                true,
                "<gray>You already have a cell!"
            )
        )
    }

    private fun sendCellNotFoundMessage(player: Player) {
        player.sendMessage(
            core.messageConfig.getMessageOr(
                "commands.cell.no-cell",
                true,
                "<gray>You don't have a cell!"
            )
        )
    }
}