package me.outspending.core.pmines

import com.google.common.collect.MapMaker
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket
import org.bukkit.Bukkit
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftPlayer
import org.bukkit.entity.Player

object PlayerHider {
    private val hiddenPlayers: MutableMap<Player, Collection<Player>> = MapMaker().weakKeys().makeMap()

    fun isHidden(player: Player, hiddenPlayer: Player): Boolean {
        val hidden: Collection<Player>? = hiddenPlayers[player]
        return hidden?.contains(hiddenPlayer) ?: false
    }

    fun hidePlayer(player: Player, hiddenPlayer: Player, addToMap: Boolean) {
        val world = hiddenPlayer.world
        if (world.name == "Testing" && !isHidden(player, hiddenPlayer)) { // TODO: Gotta change this in the future since i doubt the world will be called "Testing"
            val connection = (player as CraftPlayer).handle.connection
            connection.send(ClientboundRemoveEntitiesPacket(hiddenPlayer.entityId))

            if (addToMap) {
                hiddenPlayers[player] = listOf(hiddenPlayer)
            }
        }
    }

    fun hidePlayers(player: Player, hiddenPlayers: List<Player>) {
        hiddenPlayers.forEach { hidePlayer(player, it, false) }
        this.hiddenPlayers[player] = hiddenPlayers
    }

    fun hideNonMembers(pmine: PrivateMine) {
        val collection = pmine.getMemberCollection()

        val allPlayers = Bukkit.getOnlinePlayers()

        val members = collection.getAllOnlineMembers()
        val nonMembers = allPlayers.filter { it !in members }

        members.forEach { member -> hidePlayers(member, nonMembers) }
    }
}
