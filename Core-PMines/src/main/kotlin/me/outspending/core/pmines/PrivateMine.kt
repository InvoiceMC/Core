package me.outspending.core.pmines

import org.bukkit.Location
import org.bukkit.entity.Player
import java.util.UUID
import kotlin.reflect.jvm.internal.impl.descriptors.Visibilities.Private

interface PrivateMine {

    companion object {
        fun createMine(name: String, owner: Player): PrivateMine {
            // TODO: Update these to be correct
            val defaultMine = Mine.default() ?: throw NullPointerException("Default mine is null")
            val location = owner.location

            return PrivateMineImpl(name, owner, mutableListOf(), location, defaultMine)
        }

        fun createMine(name: String, owner: Player, members: MutableList<UUID>, spawn: Location, mine: Mine): PrivateMine {
            return PrivateMineImpl(name, owner, members, spawn, mine)
        }
    }

    fun getMineName(): String
    fun getMineOwner(): Player
    fun getMineMembers(): MutableList<UUID>
    fun getMineSpawn(): Location
    fun getMine(): Mine

    fun addMember(executedPlayer: Player, newMember: Player)
    fun removeMember(executedPlayer: Player, member: Player)

    fun joinMine(player: Player)
    fun leaveMine(player: Player)
    fun disbandMine()

    fun showInfo(player: Player)
    fun resetMine()

    fun increaseMineSize(size: Int)

    fun updatePackets(player: Player) // Used for when the player does /pmine home
    fun updateMine() // This will update the bedrock and mine

    fun isMember(player: UUID): Boolean
    fun isOwner(player: UUID): Boolean

}