package me.outspending.core.crates.impl.test

import de.tr7zw.changeme.nbtapi.NBTBlock
import me.outspending.core.Utilities
import me.outspending.core.crates.types.ICrate
import me.outspending.core.crates.types.IReward
import me.outspending.core.misc.WeightedCollection
import me.outspending.core.misc.items.ItemCreator
import org.bukkit.*
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class TestCrate() : ICrate {

    private var rewards = WeightedCollection<IReward>()
    private val name = "Test Crate"
    private val key = ItemCreator(Material.TRIPWIRE_HOOK).name("<main>$name Key").create()
    private val location = Utilities.toLocation(Bukkit.getWorld("world")!!, 1, 83, 2)
    override fun openCrate(p: Player, isSneaking: Boolean) {
        doRewards(p, isSneaking, rewards)
    }

    override fun getLocation(): Location = location

    override fun setupCrate() {
        setupRewards("me.outspending.core.gameplay.crates.impl.test.rewards")
        location.clone().block.type = Material.RED_STAINED_GLASS
        location.clone().add(0.0, -1.0, 0.0).block.type = Material.END_ROD
        NBTBlock(location.block).data.setString("crate", name)
        startParticles(Particle.DustOptions(Color.fromRGB(255, 0, 0), 1f))
    }

    override fun unload() {
        location.clone().block.type = Material.AIR
        location.clone().add(0.0, -1.0, 0.0).block.type = Material.AIR
        stopParticles()
    }

    override fun setRewards(rewards: WeightedCollection<IReward>) {
        this.rewards = rewards
    }

    override fun getDisplayName(): String = name

    override fun getItemKey(): ItemStack = key
}