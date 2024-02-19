package me.outspending.core.crates.types

import me.outspending.core.crates.cratesHandler
import me.outspending.core.crates.particles.SpiralParticles
import me.outspending.core.helpers.FormatHelper.Companion.parse
import me.outspending.core.misc.WeightedCollection
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Particle.DustOptions
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.util.Vector
import org.reflections.Reflections

interface ICrate {

    fun openCrate(p: Player, isSneaking: Boolean)

    fun doRewards(p: Player, isSneaking: Boolean, rewards: WeightedCollection<IReward>) {
        var amount = 1
        if (isSneaking) amount = getKeyAmount(p)
        val wonRewards = HashMap<String, Int>()
        repeat (amount) {
            val reward = rewards.next()
            reward.giveReward(p)
            wonRewards[reward.getName()] = wonRewards.getOrDefault(reward.getName(), 0) + 1
            p.inventory.removeItem(getItemKey())
            if (amount == 1) {
                p.sendMessage("<gray>You opened <main>1x <second>${getDisplayName()} <gray>and won: ${reward.getName()}".parse(true))
                return
            }
        }
        p.sendMessage("<gray>You opened <main>${amount}x <second>${getDisplayName()} <gray>and won: ".parse(true))
        for (reward in wonRewards.keys) {
            p.sendMessage("<gray>${wonRewards[reward]}x <main>${reward}".parse(true))
        }
    }

    fun setupCrate() {
        println("??")
        Bukkit.broadcast("<gray>Setting up billboard for ${getDisplayName()}...".parse(true))
    }

    fun getKeyAmount(p: Player): Int {
        var amount = 0
        for (item in p.inventory.contents) {
            if (item != null) {
                if (item.isSimilar(getItemKey())) {
                    amount += item.amount
                }
            }
        }
        return amount
    }

    fun setupRewards(path: String) {
        val pkg = "me.outspending.core.crates.impl.${path}.rewards"
        val collection = WeightedCollection<IReward>()
        Reflections(pkg).getSubTypesOf(IReward::class.java).forEach {
            val reward = it.getDeclaredConstructor().newInstance() as IReward
            println("Found reward: ${reward.getName()}")
            collection.add(reward.getChance(), reward)
        }
        setRewards(collection)
    }

    fun getRewards(): WeightedCollection<IReward>

    fun startParticles(options: DustOptions) {
        cratesHandler.tasks[getDisplayName()] = mutableListOf()
        cratesHandler.tasks[getDisplayName()]?.add(SpiralParticles(getLocation(), Vector(0.0, 1.0, 0.0), options))
    }

    fun stopParticles() {
        cratesHandler.tasks[getDisplayName()]?.forEach { it.cancel() }
        cratesHandler.tasks[getDisplayName()]?.clear()

    }

    fun unload()

    fun setRewards(rewards: WeightedCollection<IReward>)

    fun getLocation(): Location

    fun getDisplayName(): String

    fun getItemKey(): ItemStack
}