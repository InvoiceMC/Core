package me.outspending.core.leaderboards

import me.outspending.core.core
import me.outspending.core.storage.data.PlayerData
import me.outspending.core.utils.Utilities.orIfNull
import me.outspending.core.utils.Utilities.runAsync

class LeaderboardManager {
    private fun getAllData(): List<PlayerData>? = core.database.getAllData(core.munchPlayerData)

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
