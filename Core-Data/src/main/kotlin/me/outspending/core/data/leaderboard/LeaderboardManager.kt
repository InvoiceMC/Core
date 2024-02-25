package me.outspending.core.data.leaderboard

class LeaderboardManager {
    private val leaderboardCache = LeaderboardCache()

    fun refreshLeaderboards() = leaderboardCache.resetLeaderboards()
}
