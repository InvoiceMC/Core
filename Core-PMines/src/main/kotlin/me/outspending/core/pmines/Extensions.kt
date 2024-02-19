package me.outspending.core.pmines

import me.outspending.core.data.Extensions.getData
import me.outspending.core.pmines.data.pmineDataManager
import org.bukkit.entity.Player

object Extensions {
    fun Player.hasPmine(): Boolean = this.getData().pmineName != null
    fun Player.getPmineName(): String = this.getData().pmineName!!
    fun Player.getPmine(): PrivateMine = pmineDataManager.getPmine(this.getPmineName())
}
