package me.outspending.core.pmines

import me.outspending.core.data.Extensions.getData
import me.outspending.core.helpers.FormatHelper.Companion.parse
import org.bukkit.Location
import org.bukkit.entity.Player
import java.util.*

class PrivateMineImpl internal constructor(
    val name: String,
    val owner: Player,
    val members: MutableList<UUID>,
    val spawn: Location,
    val mine: Mine
) : PrivateMine {
    override fun getMineName(): String = name
    override fun getMineOwner(): Player = owner
    override fun getMineMembers(): MutableList<UUID> = members
    override fun getMineSpawn(): Location = spawn
    override fun getMine(): Mine = mine

    private fun hasPmine(player: Player): Boolean = player.getData().pmineName != null

    override fun addMember(executedPlayer: Player, newMember: Player) {
        val executedPlayerUUID = executedPlayer.uniqueId
        val playerUUID = newMember.uniqueId
        if (!isOwner(executedPlayerUUID)) {
            executedPlayer.sendMessage("<red>You are not the owner of this mine!".parse(true))
            return
        }
        if (isMember(playerUUID)) {
            executedPlayer.sendMessage("<red>${newMember.name} is already a member of this mine!".parse(true))
            return
        }
        if (hasPmine(newMember)) {
            executedPlayer.sendMessage("<red>${newMember.name} already in a private mine!".parse(true))
            return
        }

        members.add(playerUUID) // TODO: This is temporary
        executedPlayer.sendMessage("<green>${newMember.name} has been added to your mine!".parse(true))
    }

    override fun removeMember(executedPlayer: Player, member: Player) {
        TODO("Not yet implemented")
    }

    override fun joinMine(player: Player) {
        TODO("Not yet implemented")
    }

    override fun leaveMine(player: Player) {
        TODO("Not yet implemented")
    }

    override fun disbandMine() {
        TODO("Not yet implemented")
    }

    override fun showInfo(player: Player) {
        TODO("Not yet implemented")
    }

    override fun resetMine() = mine.reset()
    override fun increaseMineSize(size: Int) = mine.expand(size)

    override fun updatePackets(player: Player) {
        TODO("Not yet implemented")
    }

    override fun updateMine() {
        TODO("Not yet implemented")
    }

    override fun isMember(player: UUID): Boolean = members.contains(player)
    override fun isOwner(player: UUID): Boolean = owner.uniqueId == player

}