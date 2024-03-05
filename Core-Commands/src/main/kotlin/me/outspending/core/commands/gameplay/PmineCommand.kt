package me.outspending.core.commands.gameplay

import com.azuyamat.maestro.common.annotations.Command
import com.azuyamat.maestro.common.annotations.SubCommand
import me.outspending.core.data.getData
import me.outspending.core.helpers.FormatHelper.Companion.parse
import me.outspending.core.pmines.PrivateMine
import me.outspending.core.pmines.data.pmineDataManager
import me.outspending.core.pmines.getPmine
import me.outspending.core.pmines.hasPmine
import net.kyori.adventure.title.Title
import org.bukkit.entity.Player

@Command(
    name = "pmine",
    description = "PMINES!?!?!?",
    aliases = ["p", "pm"]
)
class PmineCommand {

    // TODO: This was changed, therefore need to update all the sub commands to fit this, although she works for right now
    private fun checkPmine(player: Player): Boolean {
        return player.hasPmine()
    }

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

        if (pmineDataManager.hasData(name)) {
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
        pmineDataManager.addData(name, privateMine)
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

    @SubCommand("increase")
    fun increase(player: Player, amount: Int = 1) {
        if (!checkPmine(player)) return

        val pmine = player.getPmine()
        pmine.increaseMineSize(player, amount)
    }

    @SubCommand("invite")
    fun invite(player: Player, member: Player) {
        if (!checkPmine(player)) return

        val pmine = player.getPmine()
        pmine.inviteMember(player, member)
        player.sendMessage("<green>You have invited ${member.name} to your pmine!".parse())
    }

    @SubCommand("join")
    fun join(player: Player, mineName: String) {
        if (checkPmine(player)) {
            player.sendMessage("<red>You already have a pmine!".parse(true))
            return
        }

        val pmine: PrivateMine? = pmineDataManager.getDataNullable(mineName)
        if (pmine == null) {
            player.sendMessage("<red>That pmine doesn't exist!".parse(true))
        } else {
            val collection = pmine.getMemberCollection()
            collection.addMember(pmine, player)

            val owner = collection.owner
            if (owner.isOnline) {
                owner.player?.sendMessage("<green>${player.name} has joined your pmine!".parse())
            }
        }
    }
}