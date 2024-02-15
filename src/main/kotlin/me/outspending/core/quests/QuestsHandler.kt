package me.outspending.core.quests

import me.outspending.core.Utilities.getData
import me.outspending.core.Utilities.runTaskTimer
import me.outspending.core.core
import me.outspending.core.quests.data.PlayerQuest
import me.outspending.core.quests.data.Quest
import me.outspending.core.quests.data.QuestStatus
import org.bukkit.entity.Player

const val QUESTS_LIMIT = 7
const val REFRESH_RATE = 2 * 20L // Every 2 seconds

class QuestsHandler(private val player: Player) {
//    private val quests
//        get() = player.getData()?.quests ?: mutableListOf()

//    init {
//        runTaskTimer(REFRESH_RATE, REFRESH_RATE, true) {
//            if (!player.isOnline) return@runTaskTimer
//            fillQuests()
//        }
//    }

//    private fun fillQuests() {
//        if (questsAreFilled()) return

//        val questsToFill = QUESTS_LIMIT - quests.count { it.status is QuestStatus.NotStarted }
//        val possibleQuests = getPossibleQuests()

//        repeat(questsToFill) {
//            if (possibleQuests.isEmpty()) return@repeat
//            val quest = possibleQuests.random()
//            val playerQuest = PlayerQuest(quest.id)
//
//            player.getData()?.quests?.add(playerQuest)
//            possibleQuests.remove(quest)
//        }
//    }

//    private fun getPossibleQuests(): MutableList<Quest> {
//        return core.questsConfig
//            .getQuests()
//            .flatMap { it.value }
//            .filter { quest -> quests.none { it.id == quest.id } }
//            .toMutableList()
//    }
//
//    private fun questsAreFilled() = quests.count {
//        it.status is QuestStatus.NotStarted
//    } == QUESTS_LIMIT
}