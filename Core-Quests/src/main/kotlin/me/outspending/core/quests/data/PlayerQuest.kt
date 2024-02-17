package me.outspending.core.quests.data

data class PlayerQuest(
    val id: String,
    val givenTimestamp: Long,
    val status: QuestStatus = QuestStatus.NotStarted
) {
    constructor(id: String) : this(id, System.currentTimeMillis())
}

sealed class QuestStatus {
    class Completed(val timestamp: Long) : QuestStatus()
    data object InProgress : QuestStatus()
    data object NotStarted : QuestStatus()
    fun name() = this::class.simpleName
    fun isOngoing() = this is InProgress || this is NotStarted
    fun prettyName() = when (this) {
        is Completed -> "Completed"
        is InProgress -> "In Progress"
        is NotStarted -> "Not Started"
    }
}
