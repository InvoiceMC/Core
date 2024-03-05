package me.outspending.core.pmines.members

import me.outspending.core.data.getData
import me.outspending.core.helpers.FormatHelper.Companion.parse
import me.outspending.core.pmines.PrivateMine
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import java.util.UUID

class MemberCollection(val owner: OfflinePlayer, val members: MutableList<OfflinePlayer>) {
    private val invites: ArrayList<UUID> = ArrayList()

    fun getAllMembers(): List<OfflinePlayer> {
        val allMembers = mutableListOf(owner)
        allMembers.addAll(members)

        return allMembers
    }

    fun getAllOnlineMembers(): List<Player> =
        getAllMembers().filterIsInstance<Player>()


    fun inviteMember(mine: PrivateMine, player: Player) {
        val uuid = player.uniqueId
        if (invites.contains(uuid) || members.contains(player)) {
            return
        }

        val mineName = mine.getMineName()

        invites.add(uuid)
        player.sendMessage("<click:run_command:/pmine join $mineName>You have been invited to join $mineName</click>".parse()) // TODO: Make this clickable
    }

    fun addMember(mine: PrivateMine, member: Player) {
        val uuid = member.uniqueId
        if (!invites.contains(uuid) || members.contains(member)) {
            return
        }

        val mineName = mine.getMineName()
        val memberData = member.getData()
        memberData.pmineName = mineName

        invites.remove(uuid)
        members.add(member)
        member.sendMessage("<green>You have joined $mineName".parse())

        if (owner.isOnline) {
            owner.player?.sendMessage("<green>${member.name} has joined your mine".parse())
        }
    }

    fun removeMember(mine: PrivateMine, member: OfflinePlayer) {
        if (!members.contains(member)) {
            return
        }

        members.remove(member)

        if (member.isOnline) {
            member.player?.sendMessage("<red>You have left ${mine.getMineName()}".parse())
        }
    }

    fun disbandMembers() {
        getAllOnlineMembers().forEach { player ->
//            if (player.world.name == "testing") { // TODO: This is for a testing world, we need to change this later
//                // Teleport to spawn
//            }

            player.sendMessage("<red>Your mine has been disbanded".parse())
        }
    }
}