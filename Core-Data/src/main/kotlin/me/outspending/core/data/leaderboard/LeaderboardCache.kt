package me.outspending.core.data.leaderboard

import me.outspending.core.data.database
import me.outspending.core.data.munchPlayerData
import me.outspending.core.data.player.PlayerData
import me.outspending.core.holograms.Hologram
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer

private const val LEADERBOARD_SIZE: UByte = 10u
private const val LEADERBOARD_FORMAT: String = "%s - %s: %s"

class LeaderboardCache {
    companion object {
        private var cachedData: Map<OfflinePlayer, PlayerData> = mutableMapOf()
    }

    private fun getAllData(): List<PlayerData>? = database.getAllData(munchPlayerData).get()

    private fun updateCache() {
        val data = getAllData()
        cachedData = data?.associate { Bukkit.getOfflinePlayer(it.uuid) to it } ?: emptyMap()
    }

    private inline fun <T : Comparable<T>> getTop(
        crossinline selector: (PlayerData) -> T
    ): List<PlayerData> {
        val data = cachedData.values.toList()

        return data.sortedByDescending { selector(it) }.take(LEADERBOARD_SIZE.toInt())
    }

    private fun updateLeaderboard(leaderboardHologram: Hologram, startingLineIndex: Int) {
        // TODO: Actually update the hologram
    }

    fun resetLeaderboards() {
        updateCache()

        val topBalances = getTop { it.balance }
        val topGold = getTop { it.gold }
    }
}