package me.outspending.core.storage.serializers

import me.outspending.core.quests.data.PlayerQuest
import me.outspending.core.quests.data.QuestStatus
import me.outspending.munch.serializer.Serializer

class PlayerQuestSerializer: Serializer<PlayerQuest> {
    override fun getSerializerClass(): Class<PlayerQuest> = PlayerQuest::class.java
    override fun deserialize(str: String): PlayerQuest {
        val parts = str.split("|")
        return PlayerQuest(parts[0], parts[1].toLong(), parts[2].split(",").map {
            when (it) {
                "InProgress" -> QuestStatus.InProgress
                "NotStarted" -> QuestStatus.NotStarted
                else -> QuestStatus.Completed(it.toLong())
            }
        })
    }
    override fun serialize(obj: Any?): String {
        val quest = obj as PlayerQuest
        return "${quest.id}|${quest.givenTimestamp}|${quest.status.joinToString(",") { status ->
            when (status) {
                is QuestStatus.InProgress -> "InProgress"
                is QuestStatus.NotStarted -> "NotStarted"
                is QuestStatus.Completed -> status.timestamp.toString()
            }
        }}"
    }

}