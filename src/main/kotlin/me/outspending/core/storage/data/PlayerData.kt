package me.outspending.core.storage.data

data class PlayerData(
    var balance: Double,
    var gold: Int,
    var blocksBroken: Long,
    var prestige: Int,
    var multiplier: Float,
    var pmineName: String,
    var tag: String,
    var cellId: String? = null,
): Data {
    companion object {

        fun default(): PlayerData {
            return PlayerData(0.0, 0, 0, 0, 1.0f, "", "")
        }
    }
}
