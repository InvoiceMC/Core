package me.outspending.core.commands.impl.utility

import me.outspending.core.commands.CommandRegistry.commands
import me.outspending.core.commands.annotations.Command
import me.outspending.core.commands.annotations.Tab
import me.outspending.core.utils.helpers.FormatHelper.Companion.parse
import org.bukkit.command.CommandSender
import kotlin.math.min

const val COMMANDS_PER_PAGE = 5

@Command(
    name = "help",
    description = "Show help menu",
    aliases = ["h", "?"],
    permission = "core.help",
)
class HelpCommand {

    fun onCommand(sender: CommandSender, @Tab("command") commandOrPage: String?) {

        val isCommand = commandOrPage != null && commandOrPage.toIntOrNull() == null
        val allowedCommands =
            commands.filter { it.permission.isNotEmpty() && sender.hasPermission(it.permission) || it.permission.isEmpty() }

        val commandsSize = allowedCommands.size
        val maxPage = (commandsSize + COMMANDS_PER_PAGE - 1) / COMMANDS_PER_PAGE
        val page = min(commandOrPage?.toIntOrNull() ?: 1, maxPage)

        val headerInfo = if (isCommand) "$commandOrPage" else "$page/$maxPage"

        val previousPageButton =
            if (page > 1 && !isCommand) "<click:run_command:/help ${page - 1}><hover:show_text:'<gray>Previous page'><main>[<]</hover></click>" else ""
        val nextPageButton =
            if (page < maxPage && !isCommand) "<click:run_command:/help ${page + 1}><hover:show_text:'<gray>Next page'><main>[>]</hover></click>" else ""

        sender.sendMessage(
            "<gray>-----$previousPageButton----<main>Help ($headerInfo)<gray>-----$nextPageButton----".parse(
                true
            )
        )

        if (isCommand) {
            val commandInfo = allowedCommands.find { it.name.equals(commandOrPage, true) } ?: run {
                sender.sendMessage("<gray>Command not found: <main>$commandOrPage".parse(true))
                return
            }

            val name = commandInfo.name
            val description = commandInfo.description.takeIf { it.isNotEmpty() } ?: "No description"
            val usage = "/$name ${if (commandInfo.subCommands.isNotEmpty()) "<subcommand>" else ""} .."
            val permission = commandInfo.permission.takeIf { it.isNotEmpty() } ?: "None"

            sender.sendMessage(
                "<${if (permission != "None") "red" else "main"}>/$name<gray>: $description\n<gray>Usage: $usage\n<gray>Permission: $permission\n<gray>Cooldown: ${commandInfo.cooldown} seconds".parse()
            )

            val subCommands =
                commandInfo.subCommands.filter { it.value.permission.isNotEmpty() || sender.hasPermission(it.value.permission) }
            val subCommandsList = subCommands.map { subCommand ->
                val subName = subCommand.value.name
                val subDescription = subCommand.value.description.takeIf { it.isNotEmpty() } ?: "No description"
                val requiresPermission = subCommand.value.permission.isNotEmpty() || commandInfo.permission.isNotEmpty()

                "<${if (requiresPermission) "red" else "main"}>/$name $subName<gray>: $subDescription"
            }

            if (subCommandsList.isNotEmpty()) {
                sender.sendMessage("<gray>Sub commands:\n${subCommandsList.joinToString("\n")}".parse(true))
            }
            return
        }

        val startIndex = (page - 1) * COMMANDS_PER_PAGE
        val endIndex = startIndex + COMMANDS_PER_PAGE

        val shownCommands = allowedCommands.subList(startIndex, endIndex.coerceAtMost(commandsSize))
        val shownCommandsList = shownCommands.map { command ->
            val name = command.name
            val description = command.description
            val requiresPermission = command.permission.isNotEmpty()

            "<hover:show_text:'<gray>Click for more help'><click:run_command:/help $name><${if (requiresPermission) "red" else "main"}>/$name<gray>: $description</click></hover>"
        }

        sender.sendMessage(shownCommandsList.joinToString("\n").parse())
    }
}