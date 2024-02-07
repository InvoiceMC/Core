package me.outspending.core.storage.data

import me.outspending.munch.Column
import me.outspending.munch.ColumnConstraint
import me.outspending.munch.PrimaryKey
import me.outspending.munch.Table
import java.util.*

@Table
data class PlayerData(
    @PrimaryKey var uuid: UUID,
    @Column([ColumnConstraint.NOTNULL]) var balance: Double,
    @Column([ColumnConstraint.NOTNULL]) var gold: Int,
    @Column([ColumnConstraint.NOTNULL]) var blocksBroken: Long,
    @Column([ColumnConstraint.NOTNULL]) var prestige: Int,
    @Column([ColumnConstraint.NOTNULL]) var multiplier: Float,
    @Column([ColumnConstraint.NOTNULL]) var pmineName: String,
    @Column([ColumnConstraint.NOTNULL]) var tag: String,
    @Column var cellId: String? = null,
) : Data {
    constructor(uuid: UUID) : this(uuid, 0.0, 0, 0, 0, 1.0f, "", "")

    constructor() : this(UUID.fromString(""), 0.0, 0, 0, 0, 1.0f, "", "")
}
