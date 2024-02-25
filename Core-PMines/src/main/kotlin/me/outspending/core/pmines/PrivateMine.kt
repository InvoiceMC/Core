package me.outspending.core.pmines

import me.outspending.core.data.Extensions.getData
import org.bukkit.Location
import org.bukkit.entity.Player

interface PrivateMine {

    companion object {
        fun createMine(name: String, owner: Player): PrivateMine {
            // TODO: Update these to be correct
            val defaultMine = Mine.default()
            val location = Location(owner.world, 271.5, 33.0, -851.5, 90f, 0f)

            val data = owner.getData()
            data.pmineName = name

            return PrivateMineImpl(name, owner, mutableListOf(), location, defaultMine)
        }

        fun createMine(
            name: String,
            owner: Player,
            members: MutableList<Player>,
            spawn: Location,
            mine: Mine
        ): PrivateMine {
            return PrivateMineImpl(name, owner, members, spawn, mine)
        }
    }

    fun getMineName(): String
    fun getMineOwner(): Player
    fun getAllMembers(): MutableList<Player>
    fun getMineMembers(): MutableList<Player>
    fun getMineSpawn(): Location
    fun getMine(): Mine

    fun addMember(executedPlayer: Player, newMember: Player)
    fun removeMember(executedPlayer: Player, member: Player)

    fun joinMine(player: Player)
    fun leaveMine(player: Player)
    fun disbandMine()

    fun teleportToMine(player: Player)

    fun showInfo(player: Player)
    fun resetMine(player: Player)

    fun increaseMineSize(size: Int)

    fun updatePackets(player: Player) // Used for when the player does /pmine home
    fun updateMine() // This will update the bedrock and mine

    fun isInMine(player: Player): Boolean
    fun isMember(player: Player): Boolean
    fun isOwner(player: Player): Boolean

}