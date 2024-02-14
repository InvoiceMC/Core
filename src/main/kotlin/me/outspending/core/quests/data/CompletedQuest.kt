package me.outspending.core.quests.data

data class CompletedQuest(
    val id: String,
    val completions: List<Completion>
)

data class Completion(
    val amount: Int,
    val timestamp: Long
)
