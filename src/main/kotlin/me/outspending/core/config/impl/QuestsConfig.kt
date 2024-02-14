package me.outspending.core.config.impl

import me.outspending.core.config.ConfigManager
import me.outspending.core.quests.data.Reward
import me.outspending.core.quests.enums.QuestEvent
import me.outspending.core.quests.enums.RewardType
import org.bukkit.plugin.java.JavaPlugin

class QuestsConfig(plugin: JavaPlugin): ConfigManager("quests", plugin) {
    private val rawConfig = getRawConfig()

    val colors: Map<QuestEvent, String> = loadColors()
    val rewards: Map<QuestEvent, Reward> = loadRewards()
    val messages: Map<QuestEvent, String> = loadMessages()

    private fun loadColors(): Map<QuestEvent, String> {
        val section = getSection("colors") ?: return mapOf()
        return QuestEvent.entries.associateWith {
            section.get(it.name).toString()
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