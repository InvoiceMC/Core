package me.outspending.core.crates

import me.outspending.core.Utilities.delay
import me.outspending.core.crates.CratesManager.cratesHandler
import me.outspending.core.crates.particles.SpiralParticles
import me.outspending.core.crates.types.ICrate
import me.outspending.core.helpers.FormatHelper.Companion.parse
import me.outspending.core.misc.hologram.Hologram
import org.bukkit.Bukkit
import org.bukkit.entity.Entity
import org.reflections.Reflections

const val CRATES_PACKAGE = "me.outspending.core.gameplay.crates.impl"

class CratesHandler {

    private val crates: MutableMap<String, ICrate> = mutableMapOf()
    val tasks: MutableMap<String, MutableList<SpiralParticles>> = mutableMapOf()
    val billboards: MutableMap<String, Entity> = mutableMapOf()

    init {
        load()
    }

    fun reload() {
        Bukkit.broadcast("<gray>Reloading crates...".parse(true))
        unregisterAllCrates()
        load()
    }

    private fun load() {
        delay(20L) {
            Reflections(CRATES_PACKAGE).getSubTypesOf(ICrate::class.java).forEach {
                val crate = it.newInstance()
                println("Found crate: ${crate.getDisplayName()}")
                registerCrate(crate.getDisplayName(), crate)
            }
            for (name in crates.keys) {
                val crate = crates[name]!!
                println("Setting up crate: $name")
                crate.setupCrate()
                cratesHandler.billboards[name] = Hologram.createServerSideHologram(crate.getLocation().clone().add(0.5, 1.5, 0.5), listOf("<main>${crate.getDisplayName()} Crate", "<gray>Right click to open", "<gray>Left click to preview rewards"))
            }
        }
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

    private fun unregisterAllCrates() {
        for (crate in crates) {
            crate.value.unload()
            cratesHandler.billboards[crate.key]?.remove()
        }
        crates.clear()
    }

    fun getCrates(): MutableCollection<ICrate> {
        return crates.values
    }



}