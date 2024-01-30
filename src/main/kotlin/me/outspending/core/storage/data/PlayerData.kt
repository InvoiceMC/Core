package me.outspending.core.storage.data

data class PlayerData(
    @PropertyOrder(1) var balance: Double,
    @PropertyOrder(2) var gold: Int,
    @PropertyOrder(3) var blocksBroken: Long,
    @PropertyOrder(4) var prestige: Int,
    @PropertyOrder(5) var multiplier: Float,
    @PropertyOrder(6) var pmineName: String,
    @PropertyOrder(7) var tag: String,
    @PropertyOrder(8) var cellId: String? = null,
) : Data {
    constructor() : this(0.0, 0, 0, 0, 1.0f, "", "")
}

