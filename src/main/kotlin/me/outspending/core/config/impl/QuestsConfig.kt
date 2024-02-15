package me.outspending.core.config.impl

import me.outspending.core.config.ConfigManager
import me.outspending.core.quests.data.Quest
import me.outspending.core.quests.data.Reward
import me.outspending.core.quests.enums.QuestEvent
import me.outspending.core.quests.enums.RewardType
import org.bukkit.plugin.java.JavaPlugin

class QuestsConfig(plugin: JavaPlugin): ConfigManager("quests", plugin) {
    private val rawConfig = getRawConfig()

    private val rewards: Map<QuestEvent, Reward> = loadRewards()
    private val messages: Map<QuestEvent, String> = loadMessages()
    private val quests: Map<QuestEvent, List<Quest>> = loadQuests()

    fun getQuests(event: QuestEvent) = quests[event] ?: emptyList()
    fun getReward(event: QuestEvent) = rewards[event]
    fun getMessage(event: QuestEvent) = messages[event]


    private fun loadQuests(): Map<QuestEvent, List<Quest>> {
        val section = getSection("quests") ?: return mapOf()
        return QuestEvent.entries.associateWith {
            section.getStringList(it.name).map { id -> Quest(id, it) }
        }
    }

    private fun loadRewards(): Map<QuestEvent, Reward> {
        val section = getSection("rewards") ?: return mapOf()
        return QuestEvent.entries.associateWith {
            val entry = section.getConfigurationSection(it.name)
            Reward(
                type = RewardType.valueOf(entry?.getString("type") ?: "BALANCE"),
                amount = entry?.getLong("amount") ?: 1,
                shouldIncrement = entry?.getBoolean("should_increment") ?: false,
                increment = entry?.getLong("increment") ?: 0
            )
        }
    }

    private fun loadMessages(): Map<QuestEvent, String> {
        val section = getSection("messages") ?: return mapOf()
        return QuestEvent.entries.associateWith {
            section.get(it.name).toString()
        }
    }

    private fun getSection(name: String) = rawConfig.getConfigurationSection(name)
}