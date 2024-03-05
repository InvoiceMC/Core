package me.outspending.core.pmines.data

import me.outspending.core.misc.WeightedCollection
import me.outspending.core.pmines.Mine
import me.outspending.core.pmines.PrivateMine
import me.outspending.munch.Column
import me.outspending.munch.ColumnConstraint
import me.outspending.munch.PrimaryKey
import me.outspending.munch.Table
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.block.data.BlockData
import org.bukkit.entity.Player
import java.util.UUID

fun PrivateMine.toStorable(): StorablePmine {
    val mine = this.getMine()
    val memberCollection = this.getMemberCollection()
    return StorablePmine(
        this.getMineName(),
        memberCollection.owner.uniqueId,
        memberCollection.getAllMembers().map { it.uniqueId }.toMutableList(),
        this.getMineSpawn(),

        mine.getBottomLocation(),
        mine.getTopLocation(),
        mine.getBlockWeights()
    )
}


@Table("pmine_data")
data class StorablePmine(
    @PrimaryKey val name: String,
    @Column(constraints = [ColumnConstraint.NOTNULL]) val owner: UUID,
    @Column(constraints = [ColumnConstraint.NOTNULL]) val members: MutableList<UUID>,
    @Column(constraints = [ColumnConstraint.NOTNULL]) val spawn: Location,

    @Column(constraints = [ColumnConstraint.NOTNULL]) val bottom: Location,
    @Column(constraints = [ColumnConstraint.NOTNULL]) val top: Location,
    @Column(constraints = [ColumnConstraint.NOTNULL]) val blocks: WeightedCollection<BlockData>
) {
    fun toPmine(): PrivateMine {
        val mine = Mine.createMine(bottom, top, blocks)

        val owner = Bukkit.getOfflinePlayer(owner)
        val members = members.map { Bukkit.getOfflinePlayer(it) }.toMutableList()

        return PrivateMine.createMine(name, owner, members, spawn, mine)
    }
}
