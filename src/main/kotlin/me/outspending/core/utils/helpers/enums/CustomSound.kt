package me.outspending.core.utils.helpers.enums

import org.bukkit.Sound

/**
 * Enum class for custom sounds
 *
 * @param sound The sound to play
 * @param volume The volume of the sound
 * @param pitch The pitch of the sound
 */
enum class CustomSound(
    val sound: Sound,
    val volume: Float = 0.5f,
    val pitch: Float = 1f
) {
    Success(Sound.ENTITY_PLAYER_LEVELUP),
    Failure(Sound.ENTITY_VILLAGER_NO),
    Click(Sound.UI_BUTTON_CLICK),
    Open(Sound.BLOCK_CHEST_OPEN),
    Close(Sound.BLOCK_CHEST_CLOSE),
    Pop(Sound.ENTITY_ITEM_PICKUP)
}