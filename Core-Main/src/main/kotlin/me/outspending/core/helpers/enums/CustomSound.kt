package me.outspending.core.helpers.enums

import org.bukkit.Bukkit
import org.bukkit.Sound
import org.bukkit.entity.Player

open class CustomSound {
    interface CoreSound {
        fun playSound(player: Player)
        fun broadcastSound()
    }

    data class Levelup(val volume: Float = 1F, val pitch: Float = 1F) : CoreSound {
        override fun playSound(player: Player) {
            player.playSound(player.location, Sound.ITEM_TRIDENT_HIT, volume, pitch)
        }
        override fun broadcastSound() {
            Bukkit.getOnlinePlayers().forEach(this::playSound)
        }
    }

    data class Success(val volume: Float = 1F, val pitch: Float = 1F) : CoreSound {
        override fun playSound(player: Player) {
            player.playSound(player.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, volume, pitch)
        }
        override fun broadcastSound() {
            Bukkit.getOnlinePlayers().forEach(this::playSound)
        }
    }

    data class Failure(val volume: Float = 1F, val pitch: Float = 1F) : CoreSound {
        override fun playSound(player: Player) {
            player.playSound(player.location, Sound.ENTITY_VILLAGER_NO, volume, pitch)
        }
        override fun broadcastSound() {
            Bukkit.getOnlinePlayers().forEach(this::playSound)
        }
    }

    data class Ping(val volume: Float = 1F, val pitch: Float = 1F) : CoreSound {
        override fun playSound(player: Player) {
            player.playSound(player.location, Sound.BLOCK_NOTE_BLOCK_CHIME, volume, pitch)
        }
        override fun broadcastSound() {
            Bukkit.getOnlinePlayers().forEach(this::playSound)
        }
    }

    data class Bit(val volume: Float = 1F, val pitch: Float = 1F) : CoreSound {
        override fun playSound(player: Player) {
            player.playSound(player.location, Sound.BLOCK_NOTE_BLOCK_BIT, volume, pitch)
        }
        override fun broadcastSound() {
            Bukkit.getOnlinePlayers().forEach(this::playSound)
        }
    }

    data class Keyall(val volume: Float = 1F, val pitch: Float = 1F) : CoreSound {
        override fun playSound(player: Player) {
            player.playSound(player.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, volume, pitch)
            player.playSound(player.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, volume, pitch + 0.1F)
            player.playSound(player.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, volume, pitch + 0.2F)
        }

        override fun broadcastSound() {
            Bukkit.getOnlinePlayers().forEach(this::playSound)
        }
    }

    data class DataSave(val volume: Float = 1F, val pitch: Float = 1F) : CoreSound {
        override fun playSound(player: Player) {
            player.playSound(player.location, Sound.ENTITY_ENDER_DRAGON_FLAP, volume, pitch)
        }

        override fun broadcastSound() {
            Bukkit.getOnlinePlayers().forEach(this::playSound)
        }
    }
}