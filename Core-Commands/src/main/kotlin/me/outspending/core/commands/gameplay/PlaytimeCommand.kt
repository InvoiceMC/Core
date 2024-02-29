package me.outspending.core.commands.gameplay

import com.azuyamat.maestro.common.annotations.Command
import me.outspending.core.helpers.FormatHelper.Companion.parse
import org.bukkit.Statistic
import org.bukkit.entity.Player
import kotlin.time.DurationUnit
import kotlin.time.toDuration

@Command(
    name = "playtime",
    description = "View your current playtime"
)
class PlaytimeCommand {

    // TODO: Fix This
    fun onCommand(player: Player) {
        val playtime = player.getStatistic(Statistic.PLAY_ONE_MINUTE)
        val time = (playtime / 20).toDuration(DurationUnit.MILLISECONDS)

        println(playtime)
        println(time)

        player.sendMessage("Your current playtime is $time!".parse())
    }
}