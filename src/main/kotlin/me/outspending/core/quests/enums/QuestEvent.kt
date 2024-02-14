package me.outspending.core.quests.enums

import org.bukkit.event.Event
import org.bukkit.event.block.BlockBreakEvent

enum class QuestEvent(paperEvent: Class<Event>) {
    MINING(BlockBreakEvent::class.java),
    COMMAND_SENT()
}