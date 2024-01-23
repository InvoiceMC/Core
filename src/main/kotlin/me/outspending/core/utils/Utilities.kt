package me.outspending.core.utils

import me.outspending.core.Core
import me.outspending.core.instance
import me.outspending.core.storage.DataHandler
import me.outspending.core.storage.PlayerData
import me.outspending.core.utils.Utilities.format
import me.outspending.core.utils.helpers.NumberHelper
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import net.minecraft.network.protocol.Packet
import net.minecraft.server.network.ServerGamePacketListenerImpl
import org.bukkit.Bukkit
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftPlayer
import org.bukkit.entity.Player
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

object Utilities {
    @PublishedApi
    internal val THREAD_EXECUTOR: ExecutorService = Executors.newSingleThreadExecutor()

    /** Strings */
    fun String.toComponent(): Component = Core.miniMessage.deserialize(this)

    fun String.toComponent(vararg placeholders: TagResolver): Component =
        Core.miniMessage.deserialize(this, *placeholders)

    fun String.colorize(): String = ColorUtils.colorize(this)

    fun String.colorizeHex(): String = ColorUtils.colorizeHex(this)

    fun String.removeDecimal(): String = this.replace(".0", "")

    fun String.toTinyString(): String = StringUtils.tinyString(this)

    /** Numbers */
    fun Number.format(): String = NumberHelper(this).toShorten()

    fun Number.regex(): String = NumberHelper(this).toCommas()

    fun Number.fix(): String = "%.1f".format(this.toDouble())

    fun Number.toTinyNumber(): String = NumberHelper(this).toTinyNumbers()

    /** Player */
    fun Player.getData(): PlayerData? = DataHandler.getPlayerData(this.uniqueId)

    fun Player.toAudience(): Audience = Audience.audience(this)

    fun Player.getConnection(): ServerGamePacketListenerImpl? =
        (this as? CraftPlayer)?.handle?.connection

    fun Player.sendPacket(packet: Packet<*>) = this.getConnection()?.send(packet)

    /** Async */
    inline fun runAsync(crossinline block: () -> Unit) = THREAD_EXECUTOR.execute { block() }

    /** BukkitRunnables */
    inline fun delay(delay: Long, crossinline block: () -> Unit) =
        Bukkit.getScheduler().runTaskLater(instance, Runnable { block() }, delay)

    inline fun repeat(delay: Long, period: Long, crossinline block: () -> Unit) =
        Bukkit.getScheduler().runTaskTimer(instance, Runnable { block() }, delay, period)

    inline fun runTask(crossinline block: () -> Unit) =
        Bukkit.getScheduler().runTask(instance, Runnable { block() })

    inline fun runTaskAsynchronously(crossinline block: () -> Unit) =
        Bukkit.getScheduler().runTaskAsynchronously(instance, Runnable { block() })

    inline fun runTaskLater(delay: Long, crossinline block: () -> Unit) =
        Bukkit.getScheduler().runTaskLater(instance, Runnable { block() }, delay)

    inline fun runTaskLaterAsynchronously(delay: Long, crossinline block: () -> Unit) =
        Bukkit.getScheduler().runTaskLaterAsynchronously(instance, Runnable { block() }, delay)

    inline fun runTaskTimer(delay: Long, period: Long, crossinline block: () -> Unit) =
        Bukkit.getScheduler().runTaskTimer(instance, Runnable { block() }, delay, period)

    inline fun runTaskTimerAsynchronously(
        delay: Long,
        period: Long,
        crossinline block: () -> Unit
    ) =
        Bukkit.getScheduler()
            .runTaskTimerAsynchronously(instance, Runnable { block() }, delay, period)

    /** Nullables */
    fun <T> T?.orIfNull(default: () -> T): T {
        return this ?: default()
    }

    /** Progress Bars */
    fun progressBar(
        current: Int,
        max: Int,
        length: Int = 20,
        symbol: String = "|",
        completedColor: String = "<green>",
        notCompletedColor: String = "<red>",
    ): String =
        progressBar((current / max).toFloat(), length, symbol, completedColor, notCompletedColor)

    fun progressBar(
        percent: Float,
        length: Int = 20,
        symbol: String = "|",
        completedColor: String = "<green>",
        notCompletedColor: String = "<red>",
    ): String {
        val progress = (length * percent).toInt()
        return "$completedColor${symbol.repeat(progress)}$notCompletedColor${symbol.repeat(length - progress)}"
    }
}
