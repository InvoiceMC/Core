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
import org.bukkit.entity.Player
import java.util.*

class ScoreboardHandler {
    private val scoreboardMap: MutableMap<UUID, FastBoard> = mutableMapOf()
    private val scoreboardFormat: Array<String> = arrayOf(
        "",
        "<main>ɪɴꜰᴏʀᴍᴀᴛɪᴏɴ",
        "<main><b>|</b> <gray>ᴘʀᴇꜱᴛɪɢᴇ: <#c97be3>★%prestige%",
        "<main><b>|</b> <gray>ʟᴇᴠᴇʟ: <white>%level%",
        "<main><b>|</b> <dark_gray>- %progress%",
        "",
        "<main><b>|</b> <green>$%balance% <dark_green>ᴍᴏɴᴇʏ",
        "<main><b>|</b> <yellow>⛁%gold% <gold>ɢᴏʟᴅ",
        "<main><b>|</b> <gray>⛏%blocks% <dark_gray>ʙʟᴏᴄᴋꜱ",
        "",
        "<main><b>|</b> <gray>ᴘᴍɪɴᴇ: <white>%pmine%",
        "<main><b>|</b> <gray>ᴘᴍɪɴᴇ ʟᴇᴠᴇʟ: <white>N/A",
        "",
        "<white><u>Invoice</n>.Minehut.gg"
    )

    init {
        runTaskTimerAsynchronously(40, 40)  {
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

    private fun parseLine(player: Player, line: String, playerData: PlayerData, pmine: String): String {
        return line
            .replace("%player%", player.name)
            .replace("%pmine%", pmine.ifEmpty { "N/A" })
            .replace("%level%", player.level.toString())
            .replace("%progress%", "${progressBar(player.exp)} <dark_gray>[<gray>${(player.exp * 100.0).fix()}%<dark_gray>]")
            .replace("%balance%", playerData.balance.format())
            .replace("%gold%", playerData.gold.fix())
            .replace("%blocks%", playerData.blocksBroken.format())
            .replace("%prestige%", playerData.prestige.toString())
    }

    private fun updateScoreboard(board: FastBoard) {
        val player: Player = board.player

        player.getData()?.let { playerData ->
            val pmine = playerData.pmineName

            board.updateTitle("<color:#e08a19><b>ɪɴᴠᴏɪᴄᴇ".toComponent())
            board.updateLines(scoreboardFormat.map {
                val message = parseLine(player, it, playerData, pmine)
                message.toComponent()
            })
        }
    }
}
