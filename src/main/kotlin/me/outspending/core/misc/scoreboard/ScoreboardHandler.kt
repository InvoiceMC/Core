package me.outspending.core.misc.scoreboard

import fr.mrmicky.fastboard.adventure.FastBoard
import me.outspending.core.Utilities.fix
import me.outspending.core.Utilities.format
import me.outspending.core.Utilities.getData
import me.outspending.core.Utilities.progressBar
import me.outspending.core.Utilities.runTaskTimer
import me.outspending.core.misc.helpers.FormatHelper
import me.outspending.core.misc.helpers.FormatHelper.Companion.parse
import me.outspending.core.storage.registries.PlayerRegistry
import me.outspending.core.storage.data.PlayerData
import org.bukkit.entity.Player
import java.util.*

const val REFRESH_RATE = 2 * 20L // Every 2 seconds

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
        runTaskTimer(REFRESH_RATE, REFRESH_RATE, true) {
            PlayerRegistry.playerData.keys.forEach { uuid ->
                val scoreboard: FastBoard = scoreboardMap[uuid] ?: return@forEach

                updateScoreboard(scoreboard)
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
