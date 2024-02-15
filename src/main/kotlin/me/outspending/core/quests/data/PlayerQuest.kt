package me.outspending.core.quests.data

data class PlayerQuest(
    val id: String,
    val givenTimestamp: Long,
    val status: List<QuestStatus>
)

sealed class QuestStatus {
    class Completed(val timestamp: Long) : QuestStatus()
    data object InProgress : QuestStatus()
    data object NotStarted : QuestStatus()
}
