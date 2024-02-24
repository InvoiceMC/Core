package me.outspending.core.commands.gameplay

import com.azuyamat.maestro.bukkit.annotations.Command
import com.azuyamat.maestro.bukkit.annotations.SubCommand
import me.outspending.core.data.Extensions.getData
import me.outspending.core.helpers.FormatHelper.Companion.parse
import me.outspending.core.pmines.data.pmineDataManager
import me.outspending.core.pmines.Extensions.getPmine
import me.outspending.core.pmines.Extensions.hasPmine
import me.outspending.core.pmines.PrivateMine
import net.kyori.adventure.title.Title
import org.bukkit.entity.Player

private fun checkPmine(player: Player): Boolean {
    if (!player.hasPmine()) {
        player.sendMessage("<red>You don't have a pmine!".parse(true))
        return false
    }
    return true
}

@Command(
    name = "pmine",
    description = "PMINES!?!?!?",
    aliases = ["p", "pm"]
)
class PmineCommand {

    fun onCommand(player: Player) {
        player.sendMessage("Pmines!")
    }

    @SubCommand("create")
    fun create(player: Player, name: String?) {
        if (checkPmine(player)) {
            player.sendMessage("<red>You already have a pmine!".parse(true))
            return
        }

        if (name == null) {
            player.sendMessage("<red>You need to specify a name for the pmine!".parse(true))
            return
        }

        if (name.length > 12) {
            player.sendMessage("<red>The name of the pmine can't be longer than 12 characters!".parse(true))
            return
        }

        if (pmineDataManager.hasPmineName(name)) {
            player.sendMessage("<red>A pmine with that name already exists!".parse(true))
            return
        }

        player.showTitle(
            Title.title(
                "<main>ᴘᴍɪɴᴇ".parse(),
                "<gray>Creating Pmine named <second>$name".parse()
            )
        )

        val privateMine = PrivateMine.createMine(name, player)
        pmineDataManager.addPmine(privateMine)
        println(pmineDataManager.getPmineNames())
        privateMine.resetMine(player)
    }

    @SubCommand("info")
    fun info(player: Player) {
        if (!checkPmine(player)) return

        val pmine = player.getPmine()
        pmine.showInfo(player)
    }

    @SubCommand("home")
    fun home(player: Player) {
        if (!checkPmine(player)) return

        val pmine = player.getPmine()
        pmine.teleportToMine(player)
    }

    @SubCommand("test")
    fun test(player: Player) {
        if (!checkPmine(player)) return

        val data = player.getData()
        data.pmineName = null
    }

    @SubCommand("disband")
    fun disband(player: Player) {
        if (!checkPmine(player)) return
        val pmine = player.getPmine()

        if (!pmine.isOwner(player)) {
            player.sendMessage("<red>You are not the owner of this pmine!".parse(true))
            return
        }

        pmine.disbandMine()
    }

    @SubCommand("reset")
    fun reset(player: Player) {
        if (!checkPmine(player)) return

        val pmine = player.getPmine()
        pmine.resetMine(player)
    }
}