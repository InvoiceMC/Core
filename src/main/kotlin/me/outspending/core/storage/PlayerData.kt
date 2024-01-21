package me.outspending.core.storage

data class PlayerData(
    var balance: Double,
    var gold: Int,
    var blocksBroken: Int,
    var prestige: Int,
    var multiplier: Float,
    var pmineName: String,
    var tag: String
) {
    companion object {

        fun default(): PlayerData {
            return PlayerData(0.0, 0, 0, 0, 1.0f, "", "")
        }
    }
}
