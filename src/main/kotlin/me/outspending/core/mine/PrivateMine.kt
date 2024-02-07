package me.outspending.core.mine

import me.outspending.core.utils.Utilities.toAudience
import me.outspending.core.utils.helpers.FormatHelper.Companion.parse
import net.kyori.adventure.title.Title
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.util.BoundingBox
import org.jetbrains.annotations.Range

// Default values
const val MAX_SIZE: Int = 75

class PrivateMine(
    val name: String,
    var spawnLocation: Location,
    val boundingBox: BoundingBox,
    var members: MutableList<Player>,
    var mine: Mine,
    var xp: Long = 0,
    var maxXP: Long = 1000,
    var level: Short = 1,
    var tax: Short = 1
) {

    fun addMember(player: Player) {
        if (members.size >= 10) return
        members.add(player)
    }

    fun removeMember(player: Player) {
        if (!members.contains(player)) return

        // TODO: Change this to the server's spawn when it's finished.
        if (boundingBox.contains(player.location.toVector())) {
            player.teleport(spawnLocation)
        }

        members.remove(player)
    }

    fun changeTaxes(taxes: @Range(from = 0, to = 100) Short) {
        if (taxes < 0 || taxes > 100) return
        this.tax = taxes
    }

    // TODO: Change the values before server release, DON'T FORGET
    fun checkLevel() {
        xp++
        if (xp >= maxXP) {
            if (level % 5 == 0) {
                expandMine()
            }

            level++
            xp = 0
            maxXP *= 2
            members.forEach {
                it.toAudience().showTitle(Title.title(
                    "<main><bold>LEVEL UP".parse(),
                    "<gray>Your private mine has leveled up".parse()
                ))
            }
        }
    }

    fun getMineSize(): Int = mine.getMineSize()

    fun expandMine() {
        if (getMineSize() >= MAX_SIZE) return
        mine.expandMine()

        spawnLocation.add(0.0, 1.0, 0.0)
    }
}
