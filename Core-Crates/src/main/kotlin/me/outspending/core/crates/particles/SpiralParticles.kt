package me.outspending.core.crates.particles

import me.outspending.core.CoreHandler.core
import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.Particle.DustOptions
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.Vector
import kotlin.math.abs

class SpiralParticles(origin: Location, dir: Vector, private val options: DustOptions) : BukkitRunnable() {
    private var ticks = 0

    private val dir: Vector = dir.clone().normalize()
    private var origin: Location = origin.clone().add(0.5, 0.0, 0.5)
    private var currentLoc: Location = this.origin.clone()

    init {
        this.runTaskTimer(core, 0L, 1L)
    }

    override fun run() {
        if (currentLoc.world == null) {
            this.cancel()
            return
        }
        if (ticks++ >= DURATION_IN_TICKS) {
            currentLoc.y = origin.clone().y
            ticks = 0
        }

        val offset: Vector = getPerpendicular(dir).multiply(RADIUS).rotateAroundAxis(
            dir, Math.toRadians(
                ROTATION_DEGREES_PER_TICK * ticks
            )
        )
        for (i in 0..2) currentLoc.world.spawnParticle(
            Particle.REDSTONE, currentLoc.clone().add(offset), 1,
            options
        )

        currentLoc.add(dir.clone().multiply(TRAVEL_DISTANCE_PER_TICK))
    }

    private fun getPerpendicular(vec: Vector): Vector {
        var vec: Vector = vec
        if (!vec.isNormalized) vec = vec.clone().normalize()

        return if (abs(vec.z) < abs(vec.x)) Vector(vec.y, -vec.x, 0.0) else Vector(0.0, -vec.z, vec.y)
    }

    companion object {
        private const val DURATION_IN_TICKS = 35
        private const val RADIUS = 1
        private const val ROTATION_DEGREES_PER_TICK = 20.0
        private const val TRAVEL_DISTANCE_PER_TICK = 0.03
    }
}