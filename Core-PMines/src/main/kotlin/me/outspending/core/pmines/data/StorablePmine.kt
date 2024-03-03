package me.outspending.core.pmines.data

import me.outspending.core.misc.WeightedCollection
import me.outspending.core.pmines.Mine
import me.outspending.core.pmines.PrivateMine
import me.outspending.munch.Column
import me.outspending.munch.ColumnConstraint
import me.outspending.munch.PrimaryKey
import me.outspending.munch.Table
import org.bukkit.Location
import org.bukkit.block.data.BlockData
import org.bukkit.entity.Player

fun PrivateMine.toStorable(): StorablePmine {
    val mine = this.getMine()
    return StorablePmine(
        this.getMineName(),
        this.getMineOwner(),
        this.getMineMembers(),
        this.getMineSpawn(),

        mine.getBottomLocation(),
        mine.getTopLocation(),
        mine.getBlockWeights()
    )
}


@Table("pmine_data")
data class StorablePmine(
    @PrimaryKey val name: String,
    @Column(constraints = [ColumnConstraint.NOTNULL]) val owner: Player,
    @Column(constraints = [ColumnConstraint.NOTNULL]) val members: MutableList<Player>,
    @Column(constraints = [ColumnConstraint.NOTNULL]) val spawn: Location,

    @Column(constraints = [ColumnConstraint.NOTNULL]) val bottom: Location,
    @Column(constraints = [ColumnConstraint.NOTNULL]) val top: Location,
    @Column(constraints = [ColumnConstraint.NOTNULL]) val blocks: WeightedCollection<BlockData>
) {
    fun toPmine(): PrivateMine {
        val mine = Mine.createMine(bottom, top, blocks)
        return PrivateMine.createMine(name, owner, members, spawn, mine)
    }
}
