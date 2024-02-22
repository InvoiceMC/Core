package me.outspending.core.mining.shapes

import me.outspending.core.mining.PacketShape
import me.outspending.core.misc.WeightedCollection
import me.outspending.core.pmines.PrivateMine
import org.bukkit.Location
import org.bukkit.block.data.BlockData
import org.bukkit.util.Vector
import org.jetbrains.annotations.ApiStatus.Experimental

@Experimental
class CuboidShape(private val minVec: Vector, private val maxVec: Vector) : PacketShape {
    override fun process(mine: PrivateMine, blockLocation: Location?, blockData: BlockData) {
        for
    }

    override fun process(
        mine: PrivateMine,
        blockLocation: Location?,
        weightedBlockData: WeightedCollection<BlockData>
    ) {
        TODO("Not yet implemented")
    }
}
