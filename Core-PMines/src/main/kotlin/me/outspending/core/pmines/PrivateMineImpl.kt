package me.outspending.core.pmines

import me.outspending.core.data.Extensions.getData
import me.outspending.core.helpers.FormatHelper.Companion.parse
import org.bukkit.Location
import org.bukkit.entity.Player
import java.util.*

private val RESET_MESSAGE: String = listOf(
    "",
    "<main><b>ᴘᴍɪɴᴇꜱ",
    " <second><b>|</b> %1 <gray>has just reset the mine!",
    "  <gray>Total Blocks Reset: <second>%2",
    ""
).joinToString("\n")

class PrivateMineImpl internal constructor(
    val name: String,
    private val owner: Player,
    private val members: MutableList<Player>,
    private val spawn: Location,
    private val mine: Mine
) : PrivateMine {
    override fun getMineName(): String = name
    override fun getMineOwner(): Player = owner
    override fun getAllMembers(): MutableList<Player> {
        val allMembers = mutableListOf(owner)
        allMembers.addAll(members)

        return allMembers
    }

    override fun getMineMembers(): MutableList<Player> = members
    override fun getMineSpawn(): Location = spawn
    override fun getMine(): Mine = mine

    private fun hasPmine(player: Player): Boolean = player.getData().pmineName != null

    override fun addMember(executedPlayer: Player, newMember: Player) {
        if (!isOwner(executedPlayer)) {
            executedPlayer.sendMessage("<red>You are not the owner of this mine!".parse(true))
            return
        }
        if (isMember(newMember)) {
            executedPlayer.sendMessage("<red>${newMember.name} is already a member of this mine!".parse(true))
            return
        }
        if (hasPmine(newMember)) {
            executedPlayer.sendMessage("<red>${newMember.name} already in a private mine!".parse(true))
            return
        }

        members.add(newMember) // TODO: This is temporary
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
        getAllMembers().forEach {
            val data = it.getData()
            data.pmineName = null

//            if (isInMine(it)) {
//                // TODO: Teleport player to spawn
//            }

            it.sendMessage("<red>Your mine has been disbanded!".parse(true))
        }

        TODO("Not yet implemented")
    }

    override fun showInfo(player: Player) {
        TODO("Not yet implemented")
    }

    override fun resetMine() {
        val changedBlocks = mine.reset()

        val message = RESET_MESSAGE.format(name, changedBlocks)
        getAllMembers()
            .filter { isInMine(it) }
            .forEach {
                it.sendMessage(message)
            }
    }

    override fun increaseMineSize(size: Int) = mine.expand(size)
    override fun updatePackets(player: Player) = player.sendMultiBlockChange(mine.getBlocks())

    override fun updateMine() {
        TODO("Not yet implemented")
    }

    override fun isInMine(player: Player): Boolean = player.world.name == "pmines"
    override fun isMember(player: Player): Boolean = members.contains(player)
    override fun isOwner(player: Player): Boolean = owner.uniqueId == player.uniqueId

}