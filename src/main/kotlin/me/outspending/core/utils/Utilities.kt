package me.outspending.core.utils

import java.lang.Runnable
import kotlinx.coroutines.*
import me.outspending.core.Core
import me.outspending.core.storage.DataHandler
import me.outspending.core.storage.PlayerData
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import net.minecraft.network.protocol.Packet
import net.minecraft.server.network.ServerGamePacketListenerImpl
import org.bukkit.Bukkit
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftPlayer
import org.bukkit.entity.Player
import java.util.concurrent.Executors

class Utilities {
    companion object {
        /** Strings */
        fun String.toComponent(): Component = Core.miniMessage.deserialize(this)

        fun String.toComponent(vararg placeholders: TagResolver): Component =
            Core.miniMessage.deserialize(this, *placeholders)

        fun String.colorize(): String = ColorUtils.colorize(this)

        fun String.colorizeHex(): String = ColorUtils.colorizeHex(this)

        fun String.removeDecimal(): String = this.replace(".0", "")

        fun String.toTinyString(): String = StringUtils.tinyString(this)

        /** Numbers */
        fun Number.format(): String = NumberUtils.format(this.toDouble())

        fun Number.regex(): String = NumberUtils.regex(this.toDouble())

        fun Number.fix(): String = NumberUtils.fix(this.toDouble())

        fun Number.toTinyNumber(): String = NumberUtils.tinyNumbers(this.toInt())

        /** Player */
        fun Player.getData(): PlayerData? = DataHandler.getPlayerData(this.uniqueId)

        fun Player.toAudience(): Audience = Audience.audience(this)

        fun Player.getConnection(): ServerGamePacketListenerImpl? =
            (this as? CraftPlayer)?.handle?.connection

        fun Player.sendPacket(packet: Packet<*>) = this.getConnection()?.send(packet)
    }
}

fun <T> T?.orIfNull(default: () -> T): T {
    return this ?: default()
}

inline fun delay(delay: Long, crossinline block: () -> Unit) =
    Bukkit.getScheduler().runTaskLater(Core.instance, Runnable { block() }, delay)

inline fun repeat(delay: Long, period: Long, crossinline block: () -> Unit) =
    Bukkit.getScheduler().runTaskTimer(Core.instance, Runnable { block() }, delay, period)

inline fun runAsync(crossinline block: () -> Unit) {
    object : Thread() {
        override fun run() = block()
    }.start()
}

fun progressBar(
    current: Int,
    max: Int,
    length: Int = 5,
    symbol: String = "■",
    completedColor: String = "&a",
    notCompletedColor: String = "&c",
): String =
    progressBar((current / max).toFloat(), length, symbol, completedColor, notCompletedColor)

fun progressBar(
    percent: Float,
    length: Int = 20,
    symbol: String = "|",
    completedColor: String = "&a",
    notCompletedColor: String = "&c",
): String {
    val progress = (length * percent).toInt()
    return "$completedColor${symbol.repeat(progress)}$notCompletedColor${symbol.repeat(length - progress)}"
}
