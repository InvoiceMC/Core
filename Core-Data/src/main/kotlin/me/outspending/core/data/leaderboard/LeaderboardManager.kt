package me.outspending.core.data.leaderboard

import me.outspending.core.data.database
import me.outspending.core.data.munchPlayerData
import me.outspending.core.data.player.PlayerData
import me.outspending.core.orIfNull
import me.outspending.core.runAsync

// Gotta redo this whole class since new Munch System
class LeaderboardManager {
    private fun getAllData(): List<PlayerData>? = database.getAllData(munchPlayerData).get()

    private inline fun <T : Comparable<T>> getTop(
        data: List<PlayerData>?,
        indexes: Int = 10,
        crossinline selector: (PlayerData) -> T,
    ): List<PlayerData>? {
        require(indexes > 0) { "Indexes must be greater than 0!" }

        return data.orIfNull { getAllData() }?.sortedByDescending { selector(it) }?.take(indexes)
    }

    fun getTopBalance(
        data: List<PlayerData>?,
        indexes: Int = 10,
    ): List<PlayerData>? = getTop(data, indexes) { it.balance }

    fun getTopGold(
        data: List<PlayerData>?,
        indexes: Int = 10,
    ): List<PlayerData>? = getTop(data, indexes) { it.gold }

    fun refreshLeaderboards() {
        runAsync {
            val data = getAllData()

            val topBalances = getTopBalance(data, 10)
            val topGold = getTopGold(data, 10)

            // TODO: Actually update leaderboards
        }
    }
}
