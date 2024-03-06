package me.outspending.core.pmines

import com.github.shynixn.mccoroutine.bukkit.launch
import kotlinx.coroutines.async
import me.outspending.core.CoreHandler.core
import me.outspending.core.data.getData
import me.outspending.core.format
import me.outspending.core.helpers.FormatHelper.Companion.parse
import me.outspending.core.pmines.members.MemberCollection
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.OfflinePlayer
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftPlayer
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
    private val memberCollection: MemberCollection,
    private val spawn: Location,
    private val mine: Mine
) : PrivateMine {

    override fun getMemberCollection(): MemberCollection = memberCollection

    override fun getMineName(): String = name

    override fun getMineSpawn(): Location = spawn

    override fun getMine(): Mine = mine

    private fun hasPmine(player: Player): Boolean = player.getData().pmineName != null

    // TODO: Recode this
    override fun inviteMember(executedPlayer: Player, newMember: Player) {
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

        memberCollection.inviteMember(this, newMember)
        executedPlayer.sendMessage(
            "<second>${newMember.name} <white>has been invited to your pmine!".parse(true)
        )
    }

    override fun joinMember(executedPlayer: Player) {
        memberCollection.addMember(this, executedPlayer)
    }

    override fun removeMember(executedPlayer: Player, member: OfflinePlayer) {
        memberCollection.removeMember(this, member)
    }

    override fun leaveMine(member: Player) {
        memberCollection.removeMember(this, member)
    }

    override fun disbandMine() {
        memberCollection.disbandMembers()
    }

    override fun teleportToMine(player: Player) {
        val currentWorld = player.location.world
        player.teleport(spawn)

        Bukkit.broadcast("<white>Updating packets for <second>${player.name}".parse())
        PlayerHider.hideNonMembers(this)
        updatePackets(player)

//        if (currentWorld.name != "testing") {
//            Bukkit.broadcast("<white>Updating packets for <second>${player.name}".parse())
//            PlayerHider.hideNonMembers(this)
//            updatePackets(player)
//        }
    }

    override fun showInfo(player: Player) {
        val ownerName = memberCollection.owner.name
        val memberCount = memberCollection.getAllMembers().size
        val blockCount = mine.getBlocks().size

        val message = listOf(
            "",
            "<main><b>ɪɴꜰᴏʀᴍᴀᴛɪᴏɴ",
            " <second><b>|<reset> <gray>Owner: <second>$ownerName",
            " <second><b>|<reset> <gray>Members: <second>$memberCount / 6",
            " <second><b>|<reset> <gray>Blocks: <second>$blockCount",
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
            memberCollection.getAllOnlineMembers().forEach {
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

    override fun isMember(player: OfflinePlayer): Boolean = memberCollection.members.contains(player)

    override fun isOwner(player: OfflinePlayer): Boolean = memberCollection.owner.uniqueId == player.uniqueId
}
