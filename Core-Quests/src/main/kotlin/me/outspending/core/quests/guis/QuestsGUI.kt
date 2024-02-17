package me.outspending.core.quests.guis

import me.outspending.core.CoreHandler.core
import me.outspending.core.helpers.FormatHelper
import me.outspending.core.helpers.FormatHelper.Companion.parse
import me.outspending.core.helpers.NumberHelper
import me.outspending.core.quests.QUESTS_LIMIT
import me.outspending.core.quests.data.PlayerQuest
import me.tech.mcchestui.GUI
import me.tech.mcchestui.GUIType
import me.tech.mcchestui.item.item
import me.tech.mcchestui.utils.gui
import me.tech.mcchestui.utils.openGUI
import org.bukkit.Material
import org.bukkit.entity.Player

const val MAX_QUESTS = QUESTS_LIMIT

class QuestsGUI(private val player: Player) {
//    fun open() {
//        createGUI()
//        player.openGUI(createGUI())
//    }
//    private fun createGUI(): GUI {
//        return gui(
//            plugin = core,
//            title = "Quests".parse(),
//            type = GUIType.Chest(3),
//        ) {
//            fillBorder { item = item(Material.GRAY_STAINED_GLASS_PANE) { name = " ".parse() } }
//
//            val quests = mutableListOf<PlayerQuest>()
//            val onGoingQuests = quests.filter { it.status.isOngoing() }
//
//            for (i in 0 until MAX_QUESTS) {
//                val quest = onGoingQuests.getOrNull(i) ?: continue
//                val questId = quest.id
//                val questName = questId.replace("_", " ")
//                val event = questsConfig.getEvent(questId) ?: continue
//                val message = questsConfig.getMessage(event)
//                    ?.replace("{command}", questName) ?: continue
//                val progressBar = NumberHelper(0.0).toBar(1.0, 5, "■")
//
//                val formattedName = FormatHelper("<main>$questName").toTitleCase().parse()
//
//                slot(i + 2, 2) {
//                    item = item(Material.WRITABLE_BOOK) {
//                        name = formattedName
//                        lore = listOf(
//                            "<gray><i>(( ${quest.status.prettyName()} ))",
//                            " ",
//                            message,
//                            "<gray>Click for more details",
//                            "<gray>Progress: $progressBar",
//                            " ",
//                            "<dark_gray><i>ID: ${questId.uppercase()}"
//                        ).map { it.parse() }
//                    }
//                    onClick {
//                        // Open Quest Details GUI
//                    }
//                }
//            }
//        }
//    }
}