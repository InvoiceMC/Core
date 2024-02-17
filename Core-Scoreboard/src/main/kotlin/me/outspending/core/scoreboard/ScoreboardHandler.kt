package me.outspending.core.scoreboard

import fr.mrmicky.fastboard.adventure.FastBoard
import me.outspending.core.Utilities.fix
import me.outspending.core.Utilities.format
import me.outspending.core.Utilities.progressBar
import me.outspending.core.Utilities.runTaskTimer
import me.outspending.core.data.Extentions.getData
import me.outspending.core.data.PlayerRegistry
import me.outspending.core.data.player.PlayerData
import me.outspending.core.helpers.FormatHelper
import me.outspending.core.helpers.FormatHelper.Companion.parse
import org.bukkit.entity.Player
import java.util.*

const val REFRESH_RATE = 2 * 20L // Every 2 seconds
val scoreboardManager = ScoreboardHandler()

class ScoreboardHandler {
    private val scoreboardMap: MutableMap<UUID, FastBoard> = mutableMapOf()
    private val scoreboardFormat: Array<String> =
        arrayOf(
            "         <gray>ꜱᴇᴀꜱᴏɴ 1",
            "",
            "<main><bold>%player%",
            "<second><b>|</b> <gray>Prestige: <dark_gray>[%prestige%<dark_gray>]",
            "<second><b>|</b> <gray>Level: <white>%level%",
            "<second><b>|</b> <dark_gray>- %progress%",
            "",
            "<main><bold>ʙᴀʟᴀɴᴄᴇꜱ",
            "<second><b>|</b> <gray>Balance: <green>$%balance%",
            "<second><b>|</b> <gray>Gold: <yellow>⛁%gold%",
            "<second><b>|</b> <gray>Mined: ⛏%blocks%",
            "",
            "<white><u>Invoice</u>.minehut.gg     "
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
        playerData: PlayerData
    ): String {
        return line
            .replace("%player%", player.name)
            .replace("%level%", player.level.toString())
            .replace(
                "%progress%",
                "${progressBar(player.exp, 5, "■")} <dark_gray>[<gray>${(player.exp * 100.0).fix()}%<dark_gray>]"
            )
            .replace("%balance%", playerData.balance.format())
            .replace("%gold%", playerData.gold.format())
            .replace("%blocks%", playerData.blocksBroken.format())
            .replace("%prestige%", "<#c97be3>★${playerData.prestige}")
    }

    private fun updateScoreboard(board: FastBoard) {
        val player: Player = board.player

        player.getData()?.let { playerData ->
            board.updateTitle("<main><b>ɪɴᴠᴏɪᴄᴇ</b>".parse())
            board.updateLines(
                scoreboardFormat.map { toSmallCaps(parseLine(player, it, playerData)).parse(false) }
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
