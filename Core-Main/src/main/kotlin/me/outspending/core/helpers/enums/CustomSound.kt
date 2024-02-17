package me.outspending.core.helpers.enums

import org.bukkit.Sound
import org.bukkit.entity.Player

open class CustomSound {
    fun interface CoreSound {
        fun playSound(player: Player)
    }

    data class Levelup(val volume: Float = 1F, val pitch: Float = 1F) : CoreSound {
        override fun playSound(player: Player) {
            player.playSound(player.location, Sound.ENTITY_PLAYER_LEVELUP, volume, pitch)
        }
    }

    data class Success(val volume: Float = 1F, val pitch: Float = 1F) : CoreSound {
        override fun playSound(player: Player) {
            player.playSound(player.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, volume, pitch)
        }
    }

    data class Failure(val volume: Float = 1F, val pitch: Float = 1F) : CoreSound {
        override fun playSound(player: Player) {
            player.playSound(player.location, Sound.ENTITY_VILLAGER_NO, volume, pitch)
        }
    }

    data class Ping(val volume: Float = 1F, val pitch: Float = 1F) : CoreSound {
        override fun playSound(player: Player) {
            player.playSound(player.location, Sound.BLOCK_NOTE_BLOCK_PLING, volume, pitch)
        }
    }

    data class Bit(val volume: Float = 1F, val pitch: Float = 1F) : CoreSound {
        override fun playSound(player: Player) {
            player.playSound(player.location, Sound.BLOCK_NOTE_BLOCK_BIT, volume, pitch)
        }
    }

}