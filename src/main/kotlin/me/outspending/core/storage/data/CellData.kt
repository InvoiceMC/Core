package me.outspending.core.storage.data

import me.outspending.core.storage.enums.CellRank
import me.outspending.core.storage.registries.CellRegistry
import me.outspending.munch.Column
import me.outspending.munch.ColumnConstraint
import me.outspending.munch.PrimaryKey
import me.outspending.munch.Table
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.util.BoundingBox
import java.util.*

@Table
data class CellData(
    @PrimaryKey var id: String,
    @Column(constraints = [ColumnConstraint.NOTNULL]) val members: MutableList<Pair<CellRank, UUID>>,
    @Column(constraints = [ColumnConstraint.NOTNULL]) val bound: BoundingBox,
    @Column(constraints = [ColumnConstraint.NOTNULL]) var spawn: Location
) {
    constructor(id: String, owner: Player): this(
        id,
        mutableListOf(
            Pair(CellRank.LEADER, owner.uniqueId)
        ),
        CellRegistry.getNextBoundingBox(),
        owner.location
    )
}
