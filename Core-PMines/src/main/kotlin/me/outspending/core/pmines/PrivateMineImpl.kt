package me.outspending.core.pmines

import com.github.shynixn.mccoroutine.bukkit.launch
import kotlinx.coroutines.async
import me.outspending.core.CoreHandler.core
import me.outspending.core.data.getData
import me.outspending.core.format
import me.outspending.core.helpers.FormatHelper.Companion.parse
import org.bukkit.Location
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import kotlin.time.DurationUnit
import kotlin.time.measureTime
import kotlin.time.toDuration

private val RESET_MESSAGE: String =
    listOf(
            "",
            "<main><b>ᴘᴍɪɴᴇꜱ",
            " <second><b>|<reset> <gray>Your mine has just been reset",
            "  <gray>Time: <second>%s",
            "  <gray>Total Blocks Reset: <second>%s",
            ""
        )
        .joinToString("\n")

class PrivateMineImpl
internal constructor(
    val name: String,
    private val owner: OfflinePlayer,
    private val members: MutableList<OfflinePlayer>,
    private val spawn: Location,
    private val mine: Mine
) : PrivateMine {
    override fun getMineName(): String = name

    override fun getMineOwner(): OfflinePlayer = owner

    override fun getAllMembers(): List<OfflinePlayer> {
        val allMembers = mutableListOf(owner)
        allMembers.addAll(members)

        return allMembers
    }

    override fun getAllOnlineMembers(): List<Player> = getAllMembers().filterIsInstance<Player>()

    override fun getMineMembers(): List<OfflinePlayer> = members

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
            executedPlayer.sendMessage(
                "<red>${newMember.name} is already a member of this mine!".parse(true)
            )
            return
        }
        if (hasPmine(newMember)) {
            executedPlayer.sendMessage(
                "<red>${newMember.name} already in a private mine!".parse(true)
            )
            return
        }

        members.add(newMember) // TODO: This is temporary
        executedPlayer.sendMessage(
            "<green>${newMember.name} has been added to your mine!".parse(true)
        )
    }

    override fun removeMember(executedPlayer: Player, member: OfflinePlayer) {
        TODO("Not yet implemented")
    }

    override fun joinMine(player: Player) {
        TODO("Not yet implemented")
    }

    override fun leaveMine(player: Player) {
        TODO("Not yet implemented")
    }

    override fun disbandMine() {
        getAllOnlineMembers().forEach {
            val data = it.getData()
            data.pmineName = null

            //            if (isInMine(it)) {
            //                // TODO: Teleport player to spawn
            //            }

            it.sendMessage("<red>Your mine has been disbanded!".parse(true))
        }
    }

    override fun teleportToMine(player: Player) {
        if (player.location.world.name != "testing") {
            updatePackets(player)
        }

        player.teleport(spawn)
    }

    override fun showInfo(player: Player) {
        val message = listOf(
            "",
            "<main><b>ɪɴꜰᴏʀᴍᴀᴛɪᴏɴ",
            " <second><b>|<reset> <gray>Owner: <second>${owner.name}",
            " <second><b>|<reset> <gray>Members: <second>${getAllMembers().size} / 6",
            " <second><b>|<reset> <gray>Blocks: <second>${mine.getBlocks().size}",
            "",
            "<main><b>ʙʟᴏᴄᴋꜱ",
            " <second><b>|<reset> <gray>Stone: <second>75%",
            " <second><b>|<reset> <gray>Cobblestone: <second>25%",
            ""
        ).joinToString("\n")

        player.sendMessage(message.parse())
    }

    override fun resetMine(player: Player) {
        core.launch {
            var changedBlocks: Int
            val time = measureTime {
                val result: Int = async { mine.reset(this@PrivateMineImpl) }.await()
                if (result != 0) {
                    changedBlocks = result
                } else {
                    val resetTimer = mine.getResetTimeLeft().toDuration(DurationUnit.MILLISECONDS)

                    player.sendMessage("You cannot reset your mine yet! Please wait <second>$resetTimer".parse(true))
                    return@launch
                }
            }

            val region = mine.getRegion()
            val message = RESET_MESSAGE.format(time, changedBlocks.format()).parse()
            getAllOnlineMembers().forEach {
                val location = it.location
                val plrVec = location.toVector()

                if (region.contains(plrVec)) {
                    val teleportLoc = location.clone()
                    teleportLoc.y = 30.5

                    it.teleport(teleportLoc)
                }

                it.sendMessage(message)
            }
        }
    }

    override fun increaseMineSize(player: Player, size: Int) {
        mine.expand(size)

        core.launch { async { mine.forceReset(this@PrivateMineImpl) }.await() }
    }

    override fun updatePackets(player: Player) = player.sendMultiBlockChange(mine.getBlocks())

    override fun updateMine() {
        TODO("Not yet implemented")
    }

    override fun isInMine(player: Player): Boolean = player.world.name == "pmines"

    override fun isMember(player: OfflinePlayer): Boolean = members.contains(player)

    override fun isOwner(player: OfflinePlayer): Boolean = owner.uniqueId == player.uniqueId
}
