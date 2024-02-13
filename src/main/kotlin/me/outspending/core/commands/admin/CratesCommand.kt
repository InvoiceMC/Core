package me.outspending.core.commands.admin

import com.azuyamat.maestro.bukkit.annotations.Command
import com.azuyamat.maestro.bukkit.annotations.SubCommand
import me.outspending.core.core
import me.outspending.core.misc.helpers.FormatHelper.Companion.parse
import me.outspending.core.storage.registries.PlayerRegistry
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.command.CommandSender

@Command(
    name = "crate",
    permission = "core.crate",
)
class CratesCommand {

    fun onCommand(player: CommandSender) {
        if (player !is org.bukkit.entity.Player) return
        player.teleport(Location(Bukkit.getWorld("world"), 0.0, 100.0, 0.0))
        player.sendMessage("<gray>You have been teleported to the crates!".parse(true))
    }

    @SubCommand(
        name = "reload",
        permission = "core.crate.reload"
    )
    fun reload(player: CommandSender) {
        core.cratesManager.reload()
        player.sendMessage("<gray>Successfully reloaded the crates!".parse(true))
    }

    @SubCommand(
        name = "stopParticles",
        permission = "core.crate.particles"
    )
    fun particles(player: CommandSender) {
        for (crate in core.cratesManager.getCrateNames()) {
            val c = core.cratesManager.getCrate(crate)!!
            c.stopParticles()
        }
        player.sendMessage("<gray>Successfully stopped particles!".parse(true))
    }
}