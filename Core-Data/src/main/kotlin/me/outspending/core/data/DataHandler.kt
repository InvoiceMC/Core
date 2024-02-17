package me.outspending.core.data

import me.outspending.core.data.player.PlayerData
import java.util.UUID

object DataHandler {
    fun addData(uuid: UUID) {
        database.getData(munchPlayerData, uuid).thenAccept {
            val playerData: PlayerData
            if (it == null) {
                playerData = PlayerData(uuid)

                database.addData(munchPlayerData, playerData)
            } else {
                playerData = database.getData(munchPlayerData, uuid).get()!!
            }

            PlayerRegistry.addPlayer(uuid, playerData)
        }
    }

    fun removeData(uuid: UUID) {
        database.getData(munchPlayerData, uuid).thenAccept {
            it?.let { data -> database.updateData(munchPlayerData, data, uuid) }
        }

        // Remove the player's data from memory
        PlayerRegistry.removePlayer(uuid)
    }
}