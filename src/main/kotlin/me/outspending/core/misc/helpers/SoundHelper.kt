package me.outspending.core.misc.helpers

import me.outspending.core.misc.helpers.enums.CustomSound
import org.bukkit.Location
import org.bukkit.entity.Player

/**
 * Helper class for playing sounds
 * @param sound The sound to play
 * @param volume The volume of the sound
 * @param pitch The pitch of the sound
 * @constructor Creates a new SoundHelper
 * @see CustomSound
 */
class SoundHelper(
    private val sound: CustomSound,
    private val volume: Float? = null,
    private val pitch: Float? = null
) {

    /**
     * Play the sound at the given location
     * @param location The location to play the sound at
     */
    fun play(location: Location) {
        val world = location.world ?: return
        world.playSound(location, sound.sound, volume ?: sound.volume, pitch ?: sound.pitch)
    }

    /**
     * Play the sound to the given player
     * @param player The player to play the sound to
     */
    fun play(player: Player) {
        player.playSound(player.location, sound.sound, volume ?: sound.volume, pitch ?: sound.pitch)
    }

    companion object {
        fun Player.playSound(sound: CustomSound, volume: Float? = null, pitch: Float? = null) =
            SoundHelper(sound, volume, pitch).play(this)

        fun Location.playSound(sound: CustomSound, volume: Float? = null, pitch: Float? = null) =
            SoundHelper(sound, volume, pitch).play(this)
    }
}