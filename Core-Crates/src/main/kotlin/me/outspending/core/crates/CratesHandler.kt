package me.outspending.core.crates

import me.outspending.core.Utilities.delay
import me.outspending.core.crates.particles.SpiralParticles
import me.outspending.core.crates.types.ICrate
import me.outspending.core.helpers.FormatHelper.Companion.parse
import me.outspending.core.holograms.GlobalHologram
import me.outspending.core.holograms.Hologram
import org.bukkit.Bukkit
import org.bukkit.entity.Display
import org.bukkit.entity.Entity
import org.bukkit.entity.TextDisplay
import org.reflections.Reflections

const val CRATES_PACKAGE = "me.outspending.core.crates.impl"
val cratesHandler: CratesHandler = CratesHandler().load()

class CratesHandler {

    private val crates: MutableMap<String, ICrate> = mutableMapOf()
    val tasks: MutableMap<String, MutableList<SpiralParticles>> = mutableMapOf()
    val billboards: MutableMap<String, Hologram> = mutableMapOf()

    fun reload() {
        Bukkit.broadcast("<gray>Reloading crates...".parse(true))
        unregisterAllCrates()
        load()
    }

    fun load(): CratesHandler {
        delay(20L) {
            println("This has been reloadedaa?")
            for (entity in Bukkit.getWorld("world")?.entities!!) {
                if (entity !is TextDisplay) {
                    continue
                }
                if (entity.text().contains("Right click to open".parse()))
                    entity.remove()
            }
            Reflections(CRATES_PACKAGE).getSubTypesOf(ICrate::class.java).forEach {
                val crate = it.getDeclaredConstructor().newInstance() as ICrate
                println("Found crate: ${crate.getDisplayName()}")
                registerCrate(crate.getDisplayName(), crate)
            }
            for (name in crates.keys) {
                val crate = crates[name]!!
                println("Setting up crate: $name")
                crate.setupCrate()
                billboards[name] = GlobalHologram(listOf(
                    "<main>${crate.getDisplayName()}",
                    "<gray>Right click to open",
                    "<gray>Left click to preview rewards"
                ).map { it.parse() }, crate.getLocation().clone().add(0.5, 1.5, 0.5), Display.Billboard.CENTER)
            }
        }
        return this
    }

    private fun registerCrate(name: String, crate: ICrate) {
        crates[name] = crate
    }

    fun getCrate(name: String): ICrate? {
        return crates[name]
    }

    fun getCrateNames(): Set<String> {
        return crates.keys
    }

    fun unregisterCrate(name: String) {
        crates[name]!!.unload()
        crates.remove(name)
    }

    fun unregisterAllCrates() {
        for (crate in crates) {
            crate.value.unload()
            billboards[crate.key]?.kill()
        }
        crates.clear()
    }

    fun getCrates(): MutableCollection<ICrate> {
        return crates.values
    }
}