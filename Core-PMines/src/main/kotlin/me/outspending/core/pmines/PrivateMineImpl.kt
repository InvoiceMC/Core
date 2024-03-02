package me.outspending.core.pmines

import me.outspending.core.data.Extensions.getData
import me.outspending.core.format
import me.outspending.core.helpers.FormatHelper.Companion.parse
import me.outspending.core.runAsync
import org.bukkit.Location
import org.bukkit.entity.Player
import kotlin.time.DurationUnit
import kotlin.time.measureTime
import kotlin.time.toDuration

private val RESET_MESSAGE: String = listOf(
    "",
    "<main><b>ᴘᴍɪɴᴇꜱ",
    " <second><b>|<reset> <gray>Your mine has just been reset",
    "  <gray>Time: <second>%s",
    "  <gray>Total Blocks Reset: <second>%s",
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

    // TODO: Recode this
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
    }

    override fun teleportToMine(player: Player) {
        player.teleport(spawn)
    }

    override fun showInfo(player: Player) {
        TODO("Not yet implemented")
    }

    override fun resetMine(player: Player) {
        // TODO: Make this a coroutine
        runAsync {
            var changedBlocks: Int
            val time = measureTime {
                val result = mine.reset(player, this)
                if (result != null) {
                    changedBlocks = result
                } else {
                    val resetTimer = mine.getResetTimeLeft()
                        .toDuration(DurationUnit.MILLISECONDS)

                    player.sendMessage("You cannot reset your mine yet! Please wait <second>$resetTimer".parse(true))
                    return@runAsync
                }
            }

            val message = RESET_MESSAGE.format(time, changedBlocks.format()).parse()
            getAllMembers()
                .forEach {
                    it.sendMessage(message)
                }
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