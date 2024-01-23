package me.outspending.core.commands.enums

import org.bukkit.command.CommandSender
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.entity.Player

enum class SenderType(
    val cleanName: String
) {
    PLAYER("player"),
    CONSOLE("console"),
    BOTH("anyone");

    companion object {
        fun fromSender(sender: CommandSender): Any {
            return when(sender) {
                is Player -> PLAYER
                is ConsoleCommandSender -> CONSOLE
                else -> BOTH
            }
        }
    }
}