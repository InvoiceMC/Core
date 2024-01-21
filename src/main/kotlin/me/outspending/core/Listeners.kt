package me.outspending.core

import io.papermc.paper.event.player.AsyncChatEvent
import kotlinx.coroutines.async
import me.outspending.core.enchants.EnchantGUI
import me.outspending.core.storage.DataHandler
import me.outspending.core.storage.PlayerData
import me.outspending.core.utils.Utilities.Companion.format
import me.outspending.core.utils.Utilities.Companion.getData
import me.outspending.core.utils.Utilities.Companion.toComponent
import me.outspending.core.utils.Utilities.Companion.toTinyNumber
import me.outspending.core.utils.Utilities.Companion.toTinyString
import me.outspending.core.utils.runAsync
import net.kyori.adventure.key.Key
import net.kyori.adventure.sound.Sound
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import net.kyori.adventure.title.Title
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.player.*
import org.bukkit.inventory.EquipmentSlot
import java.util.*
import kotlin.time.measureTime

class Listeners : Listener {
    private val BLACKLISTED_COMMANDS: List<String> =
        listOf(
            "plugins",
            "pl",
            "bukkit:plugins",
            "help",
            "?",
            "bukkit:help",
            "bukkit:?",
            "tell",
            "me",
            "minecraft:tell",
            "minecraft:me",
            "version",
            "ver",
            "about",
            "bukkit:about",
            "say",
            "icanhasbukkit"
        )
    private val PLAYER_STATS_HOVER_MESSAGE =
        listOf(
                "",
                "<green>\$%s <dark_green>ᴍᴏɴᴇʏ",
                "<yellow>⛁%s <gold>ɢᴏʟᴅ",
                "<gray>%s <dark_gray>ʙʀᴏᴋᴇɴ",
                "<#e8d47d>%s <#debe33>ʟᴇᴠᴇʟ",
                "<#d283de>%s <#b36de8>ᴘʀᴇꜱᴛɪɢᴇ",
                ""
            )
            .joinToString("\n")

    @EventHandler
    fun onPlayerJoin(e: PlayerJoinEvent) {
        val player: Player = e.player
        val uuid: UUID = player.uniqueId

        // Join message
        val joinMessage: Component =
            if (!player.hasPlayedBefore()) {
                "<#e08a19>${player.name} <white>has joined for the first time! <gray>[<#e8b36d>#${Bukkit.getOfflinePlayers().size}<gray>]"
                    .toComponent()
            } else {
                "<gray>${player.name} has joined!".toComponent()
            }
        e.joinMessage(joinMessage)

        // Load data
        runAsync {
            val time = measureTime {
                val playerData =
                    async { Core.playerDatabase.getData(uuid) ?: PlayerData.default() }.await()
                DataHandler.addPlayer(uuid, playerData)
            }

            player.showTitle(
                Title.title(
                    "<gradient:gold:white>★★★</gradient> <#e08a19><b>DATABASE</b> <gradient:white:gold>★★★</gradient>"
                        .toComponent(),
                    "<gray><i>Finished loading your data in <#e8b36d><u>$time</u><gray>!"
                        .toComponent(),
                ),
            )

            player.playSound(
                Sound.sound(
                    Key.key("minecraft", "entity.experience_orb.pickup"),
                    Sound.Source.PLAYER,
                    1.0f,
                    1.65f,
                ),
            )

            Core.scoreboardHandler.createScoreboard(player)
        }
    }

    @EventHandler
    fun onPlayerLeave(e: PlayerQuitEvent) {
        val player: Player = e.player
        val uuid: UUID = player.uniqueId
        val map = DataHandler.playerData

        e.quitMessage(null)

        runAsync {
            map[uuid]?.let {
                async {
                        val database = Core.playerDatabase
                        if (database.hasData(uuid)) {
                            database.updateData(uuid, it)
                        } else {
                            database.createData(uuid, it)
                        }

                        DataHandler.removePlayer(uuid)
                    }
                    .await()
            }

            Core.scoreboardHandler.removeScoreboard(player)
        }
    }

    @EventHandler
    fun onChat(e: AsyncChatEvent) {
        e.renderer { player, sourceDisplayName, message, _ ->
            val displayName = Placeholder.component("displayname", sourceDisplayName)
            val msg = Placeholder.component("message", message)
            val playerData: PlayerData = player.getData() ?: PlayerData.default()
            val playersPrefix: String = Core.luckPermsProvider.userManager.getUser(player.uniqueId)?.cachedData?.metaData?.prefix ?: "<gray>"

            val hoverText: String =
                PLAYER_STATS_HOVER_MESSAGE.format(
                    playerData.balance.format(),
                    playerData.gold.format(),
                    playerData.blocksBroken.format(),
                    player.level,
                    playerData.prestige,
                )

            "<hover:show_text:'$hoverText'>$playersPrefix<displayname><gold><bold>${player.level.toTinyNumber()}</bold></hover> <gray>»<white> <message>"
                .toComponent(displayName, msg)
        }
    }

    @EventHandler
    fun onLevelup(e: PlayerLevelChangeEvent) {
        val player: Player = e.player
        val oldLevel: Int = e.oldLevel
        val newLevel: Int = e.newLevel

        if (newLevel > oldLevel) {
            val title: Title =
                Title.title(
                    "<bold><gradient:gold:white>★★★</gradient> <#e8b36d>LEVELUP <gradient:white:gold>★★★</gradient>"
                        .toComponent(),
                    "<#e08a19>$oldLevel <gray>➲ <#e8b36d>$newLevel".toComponent(),
                )

            player.showTitle(title)
            player.playSound(
                Sound.sound(
                    Key.key("minecraft", "entity.player.levelup"),
                    Sound.Source.PLAYER,
                    1.0f,
                    1.56f,
                ),
            )
        }
    }

    @EventHandler
    fun onPickaxeRightClick(e: PlayerInteractEvent) {
        if (e.hand != EquipmentSlot.HAND) return
        val player = e.player
        val action = e.action

        // Checks
        if (!player.isSneaking) return
        if (action == Action.LEFT_CLICK_BLOCK || action == Action.LEFT_CLICK_AIR) return

        // Then check if the player is holding a pickaxe and open the GUI
        if (player.inventory.itemInMainHand.type == Material.DIAMOND_PICKAXE) {
            EnchantGUI.openGUI(player)
        }
    }

    @EventHandler fun onDamage(e: EntityDamageEvent) = run { e.isCancelled = true }

    @EventHandler
    fun onCommand(e: PlayerCommandPreprocessEvent) {
        val player: Player = e.player
        val commandName: String = e.message.split(" ")[0].substring(1)

        if (player.hasPermission("core.bypassCommands")) return
        if (BLACKLISTED_COMMANDS.contains(commandName)) {
            e.isCancelled = true
            e.player.sendMessage("Unknown command. Type \"/help\" for help.".toComponent())
        }
    }
}
