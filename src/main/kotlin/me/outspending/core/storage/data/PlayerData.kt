package me.outspending.core.storage.data

import me.outspending.core.quests.data.PlayerQuest
import me.outspending.munch.Column
import me.outspending.munch.ColumnConstraint
import me.outspending.munch.PrimaryKey
import me.outspending.munch.Table
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.*

@Table
data class PlayerData(
    @PrimaryKey val uuid: UUID,
    @Column(constraints = [ColumnConstraint.NOTNULL]) var balance: Double,
    @Column(constraints = [ColumnConstraint.NOTNULL]) var gold: Double,
    @Column(constraints = [ColumnConstraint.NOTNULL]) var blocksBroken: Long,
    @Column(constraints = [ColumnConstraint.NOTNULL]) var prestige: Int,
    @Column(constraints = [ColumnConstraint.NOTNULL]) var multiplier: Float,
    @Column(constraints = [ColumnConstraint.NOTNULL]) var tag: String,
    @Column var cellId: String? = null
) {
    constructor(uuid: UUID) : this(uuid, 0.0, 0.0, 0, 0, 1.0f, "")

    fun getPlayer(): Player? = Bukkit.getPlayer(uuid)
}
