package me.outspending.core.storage.serializers

import me.outspending.core.quests.data.PlayerQuest
import me.outspending.core.quests.data.QuestStatus
import me.outspending.munch.serializer.Serializer

class PlayerQuestSerializer: Serializer<PlayerQuest> {
    override fun getSerializerClass(): Class<PlayerQuest> = PlayerQuest::class.java
    override fun deserialize(str: String): PlayerQuest {
        val parts = str.split("|")
        val id = parts[0]
        val givenTimestamp = parts[1].toLongOrNull() ?: 0L
        val status = when (parts[2]) {
            "NotStarted" -> QuestStatus.NotStarted
            "InProgress" -> QuestStatus.InProgress
            "Completed" -> QuestStatus.Completed(parts[3].toLong())
            else -> throw IllegalArgumentException("Invalid quest status")
        }
        return PlayerQuest(id, givenTimestamp, status)
    }
    override fun serialize(obj: Any?): String {
        if (obj !is PlayerQuest) throw IllegalArgumentException("Invalid object type")
        return "${obj.id}|${obj.givenTimestamp}|${obj.status.name()}${if (obj.status is QuestStatus.Completed) "|${obj.status.timestamp}" else ""}"
    }

}