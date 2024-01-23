package me.outspending.core.scoreboard

import fr.mrmicky.fastboard.adventure.FastBoard
import me.outspending.core.storage.DataHandler
import me.outspending.core.storage.PlayerData
import me.outspending.core.utils.Utilities.fix
import me.outspending.core.utils.Utilities.format
import me.outspending.core.utils.Utilities.getData
import me.outspending.core.utils.Utilities.progressBar
import me.outspending.core.utils.Utilities.runTaskTimerAsynchronously
import me.outspending.core.utils.Utilities.toComponent
import me.outspending.core.utils.helpers.FormatHelper
import me.outspending.core.utils.helpers.FormatHelper.Companion.parse
import org.bukkit.entity.Player
import java.util.*

class ScoreboardHandler {
    private val scoreboardMap: MutableMap<UUID, FastBoard> = mutableMapOf()
    private val scoreboardFormat: Array<String> =
        arrayOf(
            "",
            "<main><bold>%player%",
            "<main>▐ <gray>Prestige: <#c97be3>★%prestige%",
            "<main>▐ <gray>Level: <white>%level%",
            "<main>▐ <dark_gray>- %progress%",
            "",
            "<main>▐ <gray>Balance: <green>$%balance%",
            "<main>▐ <gray>Gold: <yellow>⛁%gold%",
            "<main>▐ <gray>Mined: ⛏%blocks%",
            "",
            "<main><bold>SERVER",
            "<main>▐ <gray>Players: <white>%player_count%<white>/<white>%max_players%",
            "<main>▐ <gray>Ping: <white>%ping%",
            "",
            "<white><u>Invoice</u>.minehut.gg"
        )

    init {
        runTaskTimerAsynchronously(40, 40) {
            DataHandler.playerData.keys.forEach { uuid ->
                val scoreboard: FastBoard? = scoreboardMap[uuid]

                if (scoreboard != null) {
                    updateScoreboard(scoreboard)
                }
            }
        }
    }

    fun createScoreboard(player: Player) {
        val uuid: UUID = player.uniqueId
        val scoreboard = FastBoard(player)

        scoreboardMap[uuid] = scoreboard
        updateScoreboard(scoreboard)
    }

    fun removeScoreboard(player: Player) {
        val uuid: UUID = player.uniqueId

        scoreboardMap[uuid]?.let { scoreboard ->
            scoreboard.delete()
            scoreboardMap.remove(uuid)
        }
    }

    private fun parseLine(
        player: Player,
        line: String,
        playerData: PlayerData,
        pmine: String
    ): String {
        return line
            .replace("%player%", player.name)
            .replace("%pmine%", pmine.ifEmpty { "N/A" })
            .replace("%level%", player.level.toString())
            .replace(
                "%progress%",
                "${progressBar(player.exp)} <dark_gray>[<gray>${(player.exp * 100.0).fix()}%<dark_gray>]"
            )
            .replace("%balance%", playerData.balance.format())
            .replace("%gold%", playerData.gold.format())
            .replace("%blocks%", playerData.blocksBroken.format())
            .replace("%prestige%", playerData.prestige.toString())
            .replace("%player_count%", player.server.onlinePlayers.size.toString())
            .replace("%max_players%", player.server.maxPlayers.toString())
            .replace("%ping%", player.ping.toString() + "ms")
    }

    private fun updateScoreboard(board: FastBoard) {
        val player: Player = board.player

        player.getData()?.let { playerData ->
            val pmine = playerData.pmineName

            board.updateTitle("<main><b>INVOICE</b> <gray>S1".parse())
            board.updateLines(
                scoreboardFormat.map { toSmallCaps(parseLine(player, it, playerData, pmine)).parse(false) }
            )
        }
    }

    // Replace all text with small caps (except if they are in a tag)
    private fun toSmallCaps(string: String): String {
        val regex = Regex("(?s)(?<=^|>)(?!@)[^<>]*(?=<|$)")
        val regex2 = Regex("@\\w+")
        return string.replace(regex) { matchResult -> FormatHelper(matchResult.value).toSmallCaps() }.replace(regex2) {
            matchResult -> matchResult.value.replace("@", "")
        }
    }
}
