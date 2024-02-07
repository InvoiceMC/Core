package me.outspending.core.utils

import com.sk89q.worldedit.EditSession
import com.sk89q.worldedit.WorldEdit
import com.sk89q.worldedit.bukkit.BukkitAdapter
import com.sk89q.worldedit.extent.Extent
import com.sk89q.worldedit.extent.clipboard.Clipboard
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader
import com.sk89q.worldedit.function.mask.BlockMask
import com.sk89q.worldedit.function.operation.Operation
import com.sk89q.worldedit.function.operation.Operations
import com.sk89q.worldedit.function.pattern.RandomPattern
import com.sk89q.worldedit.math.BlockVector3
import com.sk89q.worldedit.regions.CuboidRegion
import com.sk89q.worldedit.regions.Region
import com.sk89q.worldedit.session.ClipboardHolder
import com.sk89q.worldedit.world.block.BaseBlock
import me.outspending.core.Core
import me.outspending.core.mine.MineBlock
import org.bukkit.Location
import org.bukkit.Material
import java.io.File
import java.io.FileInputStream

// TODO: Make sure this class works correctly.
object FAWEUtils {

    // NOTE: These functions do work (they have been tested)
    @JvmStatic
    fun setBlocks(loc1: Location, loc2: Location, material: Material) =
        setBlocks(loc1, loc2, arrayOf(MineBlock(material, 100.0)))

    // NOTE: These functions do work (they have been tested)
    @JvmStatic
    fun setBlocks(loc1: Location, loc2: Location, block: MineBlock) =
        setBlocks(loc1, loc2, arrayOf(block))

    // NOTE: These functions do work (they have been tested)
    @JvmStatic
    fun setBlocks(loc1: Location, loc2: Location, blocks: Array<MineBlock>) {
        require(loc1.world == loc2.world) { "Locations must be in the same world" }

        try {
            // Create all the necessary objects
            val editSession: EditSession =
                WorldEdit.getInstance().newEditSession(BukkitAdapter.adapt(loc1.world))
            val region: Region =
                CuboidRegion(BukkitAdapter.asBlockVector(loc1), BukkitAdapter.asBlockVector(loc2))
            val pattern = RandomPattern()

            // Add all the blocks to the pattern
            blocks.forEach { block ->
                val blockType = BukkitAdapter.asBlockType(block.material)
                pattern.add(blockType, block.chance)
            }

            // Set the blocks
            editSession.setBlocks(region, pattern)
            editSession.flushQueue()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @JvmStatic
    fun setWalls(loc1: Location, loc2: Location, material: Material = Material.AIR) {
        require(loc1.world == loc2.world) { "Locations must be in the same world" }

        try {
            val editSession: EditSession =
                WorldEdit.getInstance().newEditSession(BukkitAdapter.adapt(loc1.world))
            val region =
                CuboidRegion(BukkitAdapter.asBlockVector(loc1), BukkitAdapter.asBlockVector(loc2))

            editSession.makeWalls(region, BukkitAdapter.asBlockType(material))
            editSession.flushQueue()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @JvmStatic
    fun pasteSchematic(loc: Location, schematicName: String) {
        try {
            val file = File(Core.instance.dataFolder.absoluteFile, schematicName)
            val format: ClipboardFormat? = ClipboardFormats.findByFile(file)

            format?.let { clipboard ->
                val reader: ClipboardReader = format.getReader(FileInputStream(file))
                val clipboard: Clipboard = reader.read()

                val editSession: EditSession =
                    WorldEdit.getInstance().newEditSession(BukkitAdapter.adapt(loc.world))
                val operation: Operation =
                    ClipboardHolder(clipboard)
                        .createPaste(editSession)
                        .to(BlockVector3.at(loc.x, loc.y, loc.z))
                        .ignoreAirBlocks(true)
                        .build()

                Operations.complete(operation)
                editSession.flushQueue()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @JvmStatic
    fun countBlocks(extent: Extent, region: Region, blockMask: BlockMask): Int =
        extent.countBlocks(region, blockMask)

    @JvmStatic
    fun countBlocks(extent: Extent, region: Region, vararg blocks: BaseBlock): Int {
        val blockMask = BlockMask(extent, *blocks)
        return countBlocks(extent, region, blockMask)
    }

    @JvmStatic
    fun getVolume(loc1: Location, loc2: Location): Long {
        require(loc1.world == loc2.world) { "Locations must be in the same world" }

        val region =
            CuboidRegion(BukkitAdapter.asBlockVector(loc1), BukkitAdapter.asBlockVector(loc2))
        return region.volume
    }
}
