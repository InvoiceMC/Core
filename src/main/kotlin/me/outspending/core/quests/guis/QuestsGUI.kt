package me.outspending.core.quests.guis

import me.outspending.core.core
import me.outspending.core.misc.helpers.FormatHelper
import me.outspending.core.misc.helpers.FormatHelper.Companion.parse
import me.outspending.core.misc.helpers.NumberHelper
import me.outspending.core.quests.enums.QuestEvent
import me.tech.mcchestui.GUI
import me.tech.mcchestui.GUIType
import me.tech.mcchestui.item.item
import me.tech.mcchestui.utils.gui
import me.tech.mcchestui.utils.openGUI
import org.bukkit.Material
import org.bukkit.entity.Player

const val MAX_QUESTS = 5

class QuestsGUI(private val player: Player) {
    fun open() {
        createGUI()
        player.openGUI(createGUI())
    }
    private fun createGUI(): GUI {
        return gui(
            plugin = core,
            title = "Quests".parse(),
            type = GUIType.Chest(3),
        ) {
            fillBorder { item = item(Material.GRAY_STAINED_GLASS_PANE) { name = " ".parse() } }

            val quests = core.questsConfig.getQuests(QuestEvent.COMMAND_SENT)
            val onGoingQuests = quests.subList(0, MAX_QUESTS.coerceAtMost(quests.size))

            for (i in 0 until MAX_QUESTS) {
                val quest = onGoingQuests.getOrNull(i) ?: continue
                val questId = quest.id
                val questName = questId.replace("_", " ")
                val message = core.questsConfig.getMessage(QuestEvent.COMMAND_SENT)?.replace("{command}", questName) ?: continue
                val progressBar = NumberHelper(0.0).toBar(1.0, 5, "■")

                val formattedName = FormatHelper("<main>$questName").toTitleCase().parse()

                slot(i + 2, 2) {
                    item = item(Material.WRITABLE_BOOK) {
                        name = formattedName
                        lore = listOf(
                            "<gray><i>(( Not started ))",
                            " ",
                            message,
                            "<gray>Click for more details",
                            "<gray>Progress: $progressBar",
                            " ",
                            "<dark_gray><i>ID: ${quest.event}<dark_gray>_${questId.uppercase()}"
                        ).map { it.parse() }
                    }
                    onClick {
                        // Open Quest Details GUI
                    }
                }
            }
        }
    }
}