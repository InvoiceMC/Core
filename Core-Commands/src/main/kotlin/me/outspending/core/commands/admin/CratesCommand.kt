package me.outspending.core.commands.admin

import com.azuyamat.maestro.common.annotations.Command
import com.azuyamat.maestro.common.annotations.SubCommand
import me.outspending.core.crates.cratesHandler
import me.outspending.core.helpers.FormatHelper.Companion.parse
import me.outspending.core.helpers.enums.CustomSound
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
        name = "keyall",
        permission = "core.crate.keyall"
    )
    fun keyall(
        player: CommandSender,
        amount: Int,
        key: String
    ) {
        var key = key
        key += " Crate"
        val crate = cratesHandler.getCrate(key) ?: return player.sendMessage("<red>Invalid crate key ($key)!\n\n<gray>Crates: ${cratesHandler.getCrates().map { it.getDisplayName() }}".parse(true))
        val item = crate.getItemKey().clone()
        item.amount = amount
        for (loopedPlayer in Bukkit.getOnlinePlayers()) {
            if (loopedPlayer.inventory.firstEmpty() == -1)
                loopedPlayer.world.dropItemNaturally(loopedPlayer.location, item)
            else
                loopedPlayer.inventory.addItem(item)
        }
        Bukkit.broadcast("<gray>${player.name} <gray>has given everyone <main>$amount <gray>crate keys!".parse(true))
        CustomSound.Keyall(pitch = 1.65F).broadcastSound()
    }
}