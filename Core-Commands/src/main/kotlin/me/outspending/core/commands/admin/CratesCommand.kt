package me.outspending.core.commands.admin

import com.azuyamat.maestro.bukkit.annotations.Command
import com.azuyamat.maestro.bukkit.annotations.SubCommand
import me.outspending.core.crates.cratesHandler
import me.outspending.core.helpers.FormatHelper.Companion.parse
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

@Command(
    name = "crate",
    permission = "core.crate",
)
class CratesCommand {

    fun onCommand(player: CommandSender) {
        if (player !is Player) return
        player.teleport(Location(Bukkit.getWorld("world"), 0.0, 100.0, 0.0))
        player.sendMessage("<gray>You have been teleported to the crates!".parse(true))
    }

    @SubCommand(
        name = "reload",
        permission = "core.crate.reload"
    )
    fun reload(player: CommandSender) {
        cratesHandler.reload()
        player.sendMessage("<gray>Successfully reloaded the crates!".parse(true))
    }

    @SubCommand(
        name = "stopParticles",
        permission = "core.crate.particles"
    )
    fun particles(player: CommandSender) {
        for (crate in cratesHandler.getCrateNames()) {
            val c = cratesHandler.getCrate(crate)!!
            c.stopParticles()
        }
        player.sendMessage("<gray>Successfully stopped particles!".parse(true))
    }
}