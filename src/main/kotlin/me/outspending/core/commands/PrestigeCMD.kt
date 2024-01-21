package me.outspending.core.commands

import me.outspending.core.storage.PlayerData
import me.outspending.core.utils.Utilities.Companion.getData
import me.outspending.core.utils.Utilities.Companion.toComponent
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class PrestigeCMD : Command("prestige") {
    override fun execute(player: CommandSender, p1: String, p2: Array<out String>?): Boolean {
        if (player is Player) {
            val playerData: PlayerData? = player.getData()

            playerData?.let { data ->
                val level = player.level
                val levelAmount: Int = 100 + (25 * data.prestige)
                if (level < levelAmount) {
                    val levelNeeded = levelAmount - level

                    player.sendMessage(
                        "<dark_red><b>ᴘʀᴇꜱᴛɪɢᴇ <dark_gray>➜ </b><red>You cannot prestige yet! You need <dark_red>${levelNeeded} <red>more levels"
                            .toComponent()
                    )
                    return@let
                }

                player.level = 0
                player.exp = 0.0f
                playerData.prestige += 1

                player.sendMessage(
                    "<dark_red><b>ᴘʀᴇꜱᴛɪɢᴇ <dark_gray>➜ </b><white>You have prestiged! You are now prestige <dark_red>${data.prestige}"
                        .toComponent()
                )
            }
        }
        return false
    }
}
