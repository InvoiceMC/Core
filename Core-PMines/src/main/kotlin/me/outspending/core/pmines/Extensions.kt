package me.outspending.core.pmines

import me.outspending.core.data.DataManager
import me.outspending.core.data.DatabaseManager
import me.outspending.core.data.getData
import me.outspending.core.pmines.data.pmineDataManager
import org.bukkit.entity.Player
import org.bukkit.util.BoundingBox
import org.bukkit.util.Vector

fun Player.hasPmine(): Boolean = this.getData().pmineName != null
fun Player.getPmineName(): String = this.getData().pmineName!!
fun Player.getPmine(): PrivateMine = pmineDataManager.getData(this.getPmineName())

fun BoundingBox.hasLocation(x: Int, y: Int, z: Int): Boolean {
    return x >= this.minX && x <= this.maxX && y >= this.minY && y <= this.maxY && z >= this.minZ && z <= this.maxZ;
}

fun BoundingBox.hasLocation(vector: Vector): Boolean {
    return this.hasLocation(vector.blockX, vector.blockY, vector.blockZ)
}
