package me.outspending.core.commands.admin

import com.azuyamat.maestro.bukkit.annotations.Catcher
import com.azuyamat.maestro.bukkit.annotations.Command
import com.azuyamat.maestro.bukkit.annotations.SubCommand
import me.outspending.core.core
import me.outspending.core.misc.broadcaster.BroadcastHandler.broadcastManager
import me.outspending.core.misc.broadcaster.BroadcastHandler.broadcastsConfig
import me.outspending.core.misc.helpers.FormatHelper.Companion.parse
import org.bukkit.command.CommandSender
import kotlin.time.measureTime

@Command(
    name = "broadcast",
    aliases = ["bc"],
    permission = "core.broadcast",
    description = "Manage broadcasts",
)
class BroadcastCommand {
    fun onCommand(sender: CommandSender) {
        val broadcasts =
            core.broadcastManager.broadcasts.joinToString("") {
                val protected = if (it.isProtected()) "<main>[P]" else "<gray>[P]"
                val tooltip = it.getMessages().joinToString("\n") { line -> "<gray>$line" }
                "$protected <hover:show_text:\"$tooltip\"><gray>${it.customId}</hover>\n"
            }
        sender.sendMessage(
            (
                "<gray>Broadcasts:\n" +
                    "<gray><i>(( Protected broadcasts can't be cleared ))<reset>\n" +
                    broadcasts
            ).parse(true),
        )
    }

    @SubCommand(
        name = "add",
        permission = "core.broadcast.add",
        description = "Add a broadcast (temporary)",
    )
    fun add(
        sender: CommandSender,
        customId: String,
        message: String,
    ) {
        if (core.broadcastManager.broadcasts.any { it.customId.equals(customId, ignoreCase = true) }) {
            sender.sendMessage("<red>Broadcast with custom id already exists".parse(true))
            return
        }
        core.broadcastManager.addBroadcast(customId, message)
        sender.sendMessage("<gray>Added broadcast with custom id: <main>$customId".parse(true))
    }

    @SubCommand(
        name = "remove",
        permission = "core.broadcast.remove",
        description = "Remove a broadcast",
    )
    fun remove(
        sender: CommandSender,
        customId: String,
    ) {
        broadcastsConfig.removeValue(customId)
        broadcastsConfig.save()
        broadcastManager.removeBroadcastWithCustomId(customId)
        sender.sendMessage("<gray>Removed broadcast with custom id: <main>$customId".parse(true))
    }

    @SubCommand(
        name = "reload",
        permission = "core.broadcast.reload",
        description = "Reload broadcasts from config",
    )
    fun reload(sender: CommandSender) {
        sender.sendMessage("<gray>Reloading broadcasts...".parse(true))
        val time =
            measureTime {
                broadcastsConfig.reload()
                broadcastManager.clearBroadcasts()
                broadcastManager.registerFromConfig(broadcastsConfig)
            }
        sender.sendMessage("<gray>Broadcasts reloaded in <main>$time".parse(true))
        val fromConfig =
            broadcastsConfig.getBroadcasts().map { it.key }.joinToString("") {
                "<gray>${it}\n"
            }
        sender.sendMessage(("<gray>Broadcasts from config:\n$fromConfig").parse(true))
    }

    @SubCommand(
        name = "send",
        permission = "core.broadcast.test",
        description = "Send a broadcast",
    )
    fun send(
        sender: CommandSender,
        customId: String,
    ) {
        val broadcast = core.broadcastManager.broadcasts.find { it.customId.equals(customId, ignoreCase = true) }
        if (broadcast == null) {
            sender.sendMessage("<red>Broadcast with custom id not found".parse(true))
            return
        }
        broadcast.send()
        sender.sendMessage("<gray>Sent broadcast with custom id: <main>$customId".parse(true))
    }

    @SubCommand(
        name = "message",
        permission = "core.broadcast.message",
        description = "Send a message to all players",
    )
    fun message(
        sender: CommandSender,
        @Catcher message: String,
    ) {
        core.server.onlinePlayers.forEach { it.sendMessage(message.parse()) }
        sender.sendMessage("<gray>Sent message to all players".parse(true))
    }
}
