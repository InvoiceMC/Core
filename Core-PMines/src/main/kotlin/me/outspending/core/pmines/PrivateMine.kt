package me.outspending.core.pmines

import me.outspending.core.data.getData
import me.outspending.core.holograms.PacketHologram
import me.outspending.core.pmines.members.MemberCollection
import org.bukkit.Location
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player

interface PrivateMine {

    companion object {
        fun createMine(name: String, owner: Player): PrivateMine {
            // TODO: Update these to be correct
            val defaultMine = Mine.default()
            val pmineHome = Location(owner.world, 371.5, 33.0, -850.5, 90f, 0f)

            val data = owner.getData()
            data.pmineName = name

            return createMine(name, owner, mutableListOf(), pmineHome, defaultMine)
        }

        fun createMine(
            name: String,
            owner: OfflinePlayer,
            members: MutableList<OfflinePlayer>,
            spawn: Location,
            mine: Mine
        ): PrivateMine {
            val memberCollection = MemberCollection(owner, members)
            return createMine(name, memberCollection, spawn, mine)
        }

        fun createMine(
            name: String,
            collection: MemberCollection,
            spawn: Location,
            mine: Mine
        ): PrivateMine {
            return PrivateMineImpl(name, collection, spawn, mine)
        }
    }

    fun getMemberCollection(): MemberCollection
    fun getMineName(): String
    fun getMineSpawn(): Location
    fun getMine(): Mine

    fun inviteMember(executedPlayer: Player, newMember: Player)
    fun joinMember(executedPlayer: Player)
    fun removeMember(executedPlayer: Player, member: OfflinePlayer)

    fun leaveMine(member: Player)
    fun disbandMine()

    fun teleportToMine(player: Player)

    fun showInfo(player: Player)
    fun resetMine(player: Player)

    fun increaseMineSize(player: Player, size: Int = 1)

    fun updatePackets(player: Player) // Used for when the player does /pmine home
    fun updateMine() // This will update the bedrock and mine

    fun isInMine(player: Player): Boolean
    fun isMember(player: OfflinePlayer): Boolean
    fun isOwner(player: OfflinePlayer): Boolean

}