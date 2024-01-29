package me.outspending.core.storage.data

import me.outspending.core.instance
import me.outspending.core.utils.Utilities.getData
import org.bukkit.Location
import java.util.UUID

data class CellData(
    var name: String,
    val members: MutableList<UUID>,
    val bound: CellBoundData,
    var spawnLocation: Location
) {

    fun getTotalBlocksMined() = members.map { uuid ->
        val player = instance.server.getPlayer(uuid) ?: return@map 0L // TODO: Add different impl for getting data from offline player
        val data = player.getData() ?: return@map 0L
        data.blocksBroken
    }.sum()
}
