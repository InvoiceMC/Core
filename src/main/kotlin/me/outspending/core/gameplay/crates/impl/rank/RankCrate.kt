package me.outspending.core.gameplay.crates.impl.rank

import de.tr7zw.changeme.nbtapi.NBTBlock
import me.outspending.core.Utilities
import me.outspending.core.gameplay.crates.impl.rank.rewards.CommonReward
import me.outspending.core.gameplay.crates.impl.rank.rewards.RareReward
import me.outspending.core.gameplay.crates.particles.SpiralParticles
import me.outspending.core.gameplay.crates.types.ICrate
import me.outspending.core.gameplay.crates.types.IReward
import me.outspending.core.misc.WeightedCollection
import me.outspending.core.misc.items.ItemCreator
import org.bukkit.*
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class RankCrate() : ICrate {

    private var rewards = WeightedCollection<IReward>()
    private val name = "Rank Crate"
    private val key = ItemCreator(Material.TRIPWIRE_HOOK).name("<main>$name Key").create()
    private val location = Utilities.toLocation(Bukkit.getWorld("world")!!, -3, 83, 2)
    override fun openCrate(p: Player, isSneaking: Boolean) {
        doRewards(p, isSneaking, rewards)
    }

    override fun getLocation(): Location = location

    override fun setupCrate() {
        setupRewards("me.outspending.core.gameplay.crates.impl.rank.rewards")
        location.clone().block.type = Material.WHITE_STAINED_GLASS
        location.clone().add(0.0, -1.0, 0.0).block.type = Material.END_ROD
        NBTBlock(location.block).data.setString("crate", name)
        startParticles(Particle.DustOptions(Color.fromRGB(255, 255, 255), 1f))
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