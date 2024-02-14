package me.outspending.core.quests.data

import me.outspending.core.quests.enums.QuestEvent

data class Quest(
    val id: String,
    val event: QuestEvent
)


