package me.outspending.core.heads

import me.outspending.core.heads.types.ClaimableHead
import me.outspending.core.heads.types.IHead
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.block.structure.StructureRotation

object HeadsHandler {

    val headStorage = HashMap<String, ArrayList<IHead>>()

    fun loadHeads() {
        val appleUUID = "1071ab4f63b42b0af41c20bef57612e0058fcfadeb7ce7c4e1f7de298c05154a"
        val testHeads = listOf(ClaimableHead(Location(Bukkit.getWorld("world")!!, -4.0, 82.0, 0.0), appleUUID, 4))
        headStorage["test"] = ArrayList(testHeads)
    }

}