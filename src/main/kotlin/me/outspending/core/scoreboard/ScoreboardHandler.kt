package me.outspending.core.scoreboard

import fr.mrmicky.fastboard.FastBoard
import me.outspending.core.Core
import me.outspending.core.instance
import me.outspending.core.storage.DataHandler
import me.outspending.core.utils.Utilities.colorizeHex
import me.outspending.core.utils.Utilities.fix
import me.outspending.core.utils.Utilities.format
import me.outspending.core.utils.Utilities.getData
import me.outspending.core.utils.Utilities.progressBar
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import java.util.*

class ScoreboardHandler {
    private val scoreboardMap: MutableMap<UUID, FastBoard> = mutableMapOf()

    init {
        object : BukkitRunnable() {
                override fun run() {
                    DataHandler.playerData.keys.forEach { uuid ->
                        val scoreboard: FastBoard? = scoreboardMap[uuid]

                        if (scoreboard != null) {
                            updateScoreboard(scoreboard)
                        }
                    }
                }
            }
            .runTaskTimerAsynchronously(instance, 40, 40)
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

    fun updateScoreboard(board: FastBoard) {
        val player: Player = board.player

        player.getData()?.let { playerData ->
            val pmineName = playerData.pmineName

            board.updateTitle("&#e08a19&lɪɴᴠᴏɪᴄᴇ".colorizeHex())
            board.updateLines(
                "",
                "&#e08a19ɪɴꜰᴏʀᴍᴀᴛɪᴏɴ         ".colorizeHex(),
                "&#e8b36d&l| &7ᴘʀᴇꜱᴛɪɢᴇ: &#c97be3★${playerData.prestige}".colorizeHex(),
                "&#e8b36d&l| &7ʟᴇᴠᴇʟ: &f${player.level}".colorizeHex(),
                "&#e8b36d&l| &8- &f${progressBar(player.exp)} &8[&7${(player.exp * 100.0).fix()}%&8]"
                    .colorizeHex(),
                "",
                "&#e8b36d&l| &a$${playerData.balance.format()} &2ᴍᴏɴᴇʏ".colorizeHex(),
                "&#e8b36d&l| &e⛁${playerData.gold.format()} &6ɢᴏʟᴅ".colorizeHex(),
                "&#e8b36d&l| &7${playerData.blocksBroken.format()} &8ʙʀᴏᴋᴇɴ".colorizeHex(),
                "",
                "&#e8b36d&l| &7ᴘᴍɪɴᴇ: &f${pmineName.ifEmpty { "N/A" }}".colorizeHex(),
                "&#e8b36d&l| &7ᴘᴍɪɴᴇ ʟᴇᴠᴇʟ: &fN/A".colorizeHex(),
                "",
                "&f&nInvoice&f.minehut.gg".colorizeHex(),
            )
        }
    }
}
