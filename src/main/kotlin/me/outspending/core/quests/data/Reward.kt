package me.outspending.core.quests.data

import me.outspending.core.quests.enums.RewardType

data class Reward(
    val type: RewardType,
    val amount: Long,
    val shouldIncrement: Boolean = false,
    val increment: Long = 0
)
