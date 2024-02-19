package me.outspending.core.pmines

import me.outspending.core.Utilities.runTaskLater
import me.outspending.core.helpers.FormatHelper.Companion.parse
import org.bukkit.Location
import org.bukkit.entity.Player

private const val JOIN_TIMEOUT = 60 // In Seconds

private const val MAX_MEMBERS = 7
private const val MAX_NAME_LENGTH = 12

class PrivateMine(
    private val owner: Player,
    private val name: String,
    private var spawnLocation: Location,
    private val mine: Mine,
    private val members: MutableList<Player> = mutableListOf()
) {
    companion object {
        fun createMine(owner: Player, name: String, spawnLocation: Location, mine: Mine): PrivateMine? {
            if (name.length > MAX_NAME_LENGTH) {
                owner.sendMessage("<red>Your mine name is too long!".parse(true))
                return null
            }

            return PrivateMine(owner, name, spawnLocation, mine)
        }
    }

    private val joiningPlayers: MutableList<Player> = mutableListOf()

    fun addMember(executedPlayer: Player, player: Player) {
        if (members.size >= MAX_MEMBERS) {
            executedPlayer.sendMessage("<red>You've reached the maximum amount of members!".parse(true))
        }
        if (members.contains(player)) {
            executedPlayer.sendMessage("<red>$player is already a member!".parse(true))
        }

        player.sendMessage("<green>${executedPlayer.name} has invited you to their private mine!".parse(true))

        joiningPlayers.add(player)
        runTaskLater(JOIN_TIMEOUT * 20L) {
            if (joiningPlayers.contains(player)) {
                joiningPlayers.remove(player)
                executedPlayer.sendMessage("<red>$player did not accept the invite!".parse(true))
            }
        }
    }

    fun removeMember(executedPlayer: Player, player: Player) {
        if (player == owner) {
            executedPlayer.sendMessage("<red>You cannot remove the owner from the mine!".parse(true))
            return
        }
        if (!members.contains(player)) {
            executedPlayer.sendMessage("<red>$player is not a member!".parse(true))
            return
        }

        members.remove(player)
    }

    fun disbandMine(executedPlayer: Player) {
        if (executedPlayer != owner) {
            executedPlayer.sendMessage("<red>You are not the owner of this mine!".parse(true))
            return
        }

        members.forEach { it.sendMessage("<red>The mine has been disbanded!".parse(true)) }
        members.clear()
    }

    fun leaveMine(executedPlayer: Player) {
        if (executedPlayer == owner) {
            executedPlayer.sendMessage("<red>You cannot leave your own mine!".parse(true))
            return
        }

        members.remove(executedPlayer)
    }

    fun showInfo(executedPlayer: Player) {
        val membersString = members.joinToString(", ") { it.name }
        executedPlayer.sendMessage(
            """
            <yellow>Owner: <white>${owner.name}
            <yellow>Name: <white>$name
            <yellow>Members: <white>$membersString
            <yellow>Spawn Location: <white>${spawnLocation.blockX}, ${spawnLocation.blockY}, ${spawnLocation.blockZ}
            <yellow>Volume: <white>${mine.getVolume()}
            """.trimIndent().parse(true)
        )
    }

    fun teleport(player: Player) = player.teleport(spawnLocation)

    fun resetMine(executedPlayer: Player) = mine.reset(executedPlayer)
}
