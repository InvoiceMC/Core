package me.outspending.core

import me.outspending.core.CoreHandler.core
import me.outspending.core.CoreHandler.miniMessage
import me.outspending.core.helpers.FormatHelper
import me.outspending.core.helpers.NumberHelper
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitScheduler
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

object Utilities {
    @PublishedApi
    internal val THREAD_EXECUTOR: ExecutorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 1)
    @PublishedApi internal val scheduler: BukkitScheduler = Bukkit.getScheduler()

    /** Strings */
    fun String.toComponent(): Component = FormatHelper(this).parse()

    fun String.toComponent(vararg placeholders: TagResolver): Component =
        miniMessage.deserialize(this, *placeholders)

    fun String.toUpperCase(): String =
        this.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }

    fun String.toTinyString(): String = FormatHelper(this).toSmallCaps()

    /** Numbers */
    fun Number.format(): String = NumberHelper(this).toShorten()

    fun Number.regex(): String = NumberHelper(this).toCommas()

    fun Number.fix(): String = "%.1f".format(this.toDouble())

    fun Number.toTinyNumber(): String = NumberHelper(this).toTinyNumbers()

    /** Player */
//    fun Player.getPrefix(): String =
//        core.luckPermsProvider.userManager.getUser(this.uniqueId)?.cachedData?.metaData?.prefix
//            ?: "<gray>"

    // fun Player.getLuckPermsName(): String = "${this.getPrefix()}${this.name}"

    // fun Player.getData(): PlayerData? = PlayerRegistry.getPlayerData(this.uniqueId)

    /** Async */
    inline fun runAsync(crossinline block: () -> Unit) = THREAD_EXECUTOR.execute { block() }

    /** BukkitRunnables */
    inline fun delay(delay: Long, crossinline block: () -> Unit) =
        scheduler.runTaskLater(core, Runnable { block() }, delay)

    inline fun repeat(delay: Long, period: Long, crossinline block: () -> Unit) =
        scheduler.runTaskTimer(core, Runnable { block() }, delay, period)

    inline fun runTask(runAsync: Boolean = false, crossinline block: () -> Unit) =
        if (runAsync) scheduler.runTaskAsynchronously(core, Runnable { block() })
        else scheduler.runTask(core, Runnable { block() })

    inline fun runTaskLater(delay: Long, runAsync: Boolean = false, crossinline block: () -> Unit) =
        if (runAsync) scheduler.runTaskLaterAsynchronously(core, Runnable { block() }, delay)
        else scheduler.runTaskLater(core, Runnable { block() }, delay)

    inline fun runTaskTimer(
        delay: Long,
        period: Long,
        runAsync: Boolean = false,
        crossinline block: () -> Unit
    ) =
        if (runAsync)
            scheduler.runTaskTimerAsynchronously(core, Runnable { block() }, delay, period)
        else scheduler.runTaskTimer(core, Runnable { block() }, delay, period)

    /** Nullables */
    fun <T> T?.orIfNull(default: () -> T): T = this ?: default()

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

    /** Location */
    fun toLocation(world: World, x: Int, y: Int, z: Int) =
        Location(world, x.toDouble(), y.toDouble(), z.toDouble())

    fun getStaff(): List<Player> = Bukkit.getOnlinePlayers().filter { it.hasPermission("core.staff") }

    fun getAdmins(): List<Player> = Bukkit.getOnlinePlayers().filter { it.hasPermission("core.admin") }
}
