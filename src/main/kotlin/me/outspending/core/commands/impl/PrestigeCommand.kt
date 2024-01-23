package me.outspending.core.commands.impl

import me.outspending.core.storage.PlayerData
import me.outspending.core.utils.Utilities.getData
import me.outspending.core.utils.Utilities.toComponent
import me.outspending.core.utils.helpers.FormatHelper.Companion.parse
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class PrestigeCommand {

    fun onCommand(player: Player) {
        val playerData: PlayerData? = player.getData()

        playerData?.let { data ->
            val level = player.level
            val levelAmount: Int = 100 + (25 * data.prestige)
            if (level < levelAmount) {
                val levelNeeded = levelAmount - level

                player.sendMessage(
                    "<dark_red><b>ᴘʀᴇꜱᴛɪɢᴇ <dark_gray>➜ </b><red>You cannot prestige yet! You need <dark_red>${levelNeeded} <red>more levels"
                        .parse(true)
                )
                return@let
            }

            player.level = 0
            player.exp = 0.0f
            playerData.prestige += 1

            player.sendMessage(
                "<dark_red><b>ᴘʀᴇꜱᴛɪɢᴇ <dark_gray>➜ </b><white>You have prestiged! You are now prestige <dark_red>${data.prestige}"
                    .parse(true)
            )
        }
    }
}
