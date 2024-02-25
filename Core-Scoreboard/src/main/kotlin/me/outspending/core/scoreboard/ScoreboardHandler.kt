package me.outspending.core.scoreboard

import fr.mrmicky.fastboard.adventure.FastBoard
import me.outspending.core.data.Extensions.getData
import me.outspending.core.data.player.PlayerData
import me.outspending.core.fix
import me.outspending.core.format
import me.outspending.core.helpers.FormatHelper
import me.outspending.core.helpers.FormatHelper.Companion.parse
import me.outspending.core.progressBar
import me.outspending.core.runTaskTimer
import org.bukkit.Bukkit
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
            " <second><b>|</b> <gray>ᴘʀᴇꜱᴛɪɢᴇ: <dark_gray>[%prestige%<dark_gray>]",
            " <second><b>|</b> <gray>ʟᴇᴠᴇʟ: <white>%level%",
            " <second><b>|</b> <dark_gray>- %progress%",
            "",
            "<main><bold>ʙᴀʟᴀɴᴄᴇꜱ",
            " <second><b>|</b> <gray>ʙᴀʟᴀɴᴄᴇ: <green>$%balance%",
            " <second><b>|</b> <gray>ɢᴏʟᴅ: <yellow>⛁%gold%",
            " <second><b>|</b> <gray>ᴍɪɴᴇᴅ: ⛏%blocks%",
            "",
            "<white><u>Invoice</u>.minehut.gg     "
        )

    init {
        runTaskTimer(REFRESH_RATE, REFRESH_RATE, true) {
            Bukkit.getOnlinePlayers().forEach { player ->
                val scoreboard = scoreboardMap[player.uniqueId] ?: return@forEach

                updateScoreboard(scoreboard)
            }
        }
    }

    fun createScoreboard(player: Player) {
        val scoreboard = FastBoard(player)

        scoreboardMap[player.uniqueId] = scoreboard
        updateScoreboard(scoreboard)
    }

    fun removeScoreboard(player: Player) {
        val uuid: UUID = player.uniqueId
        val scoreboard: FastBoard = scoreboardMap[uuid] ?: return

        scoreboard.delete()
        scoreboardMap.remove(uuid)
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
        val player = board.player
        val data = player.getData()

        board.apply {
            updateTitle("<main><b>ɪɴᴠᴏɪᴄᴇ</b>".parse())
            updateLines(
                scoreboardFormat.map { parseLine(player, it, data).parse() }
            )
        }
    }

    // Replace all text with small caps (except if they are in a tag)
    @Deprecated("This method is not used anymore")
    private fun toSmallCaps(string: String): String {
        val regex = Regex("(?s)(?<=^|>)(?!@)[^<>]*(?=<|$)")
        val regex2 = Regex("@\\w+")
        return string.replace(regex) { matchResult -> FormatHelper(matchResult.value).toSmallCaps() }
            .replace(regex2) { matchResult ->
                matchResult.value.replace("@", "")
            }
    }
}
