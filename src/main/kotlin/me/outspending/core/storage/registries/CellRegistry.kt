package me.outspending.core.storage.registries

import me.outspending.core.Utilities.getAdmins
import me.outspending.core.Utilities.runAsync
import me.outspending.core.core
import me.outspending.core.storage.DatabaseHandler
import me.outspending.core.storage.DatabaseHandler.munchCellData
import me.outspending.core.storage.data.CellData
import org.bukkit.Location
import org.bukkit.util.BoundingBox

const val CELLS_PER_AXIS: Int = 10
const val MINIMUM_CELL_LINEAR_SIZE: Int = 100
const val MAXIMUM_CELL_LINEAR_SIZE: Int = 200

object CellRegistry {
    val cells: MutableMap<String, CellData> = mutableMapOf()

    fun getCell(id: String): CellData? {
        return cells[id]
    }

    fun addCell(cell: CellData) {
        cells[cell.id] = cell
    }

    fun removeCell(id: String) {
        cells.remove(id)
    }

    fun updateCell(cell: CellData) {
        DatabaseHandler.database.updateData(munchCellData, cell, cell.id)
    }

    fun updateAllCells() {
        val message = core.messageConfig.getMessageOr(
            "cell.data.update-all",
            true,
            "<gray>Successfully saved cell(s) data!"
            )
        runAsync {
            DatabaseHandler.database.updateAllData(munchCellData, cells.values.toList())
        }
        getAdmins().forEach {
            it.sendMessage(message)
        }
    }

    fun getNextBoundingBox(): BoundingBox {
        val location = getNextPlacement()
        val halfSize = MINIMUM_CELL_LINEAR_SIZE / 2
        val minX = location.x - halfSize
        val maxX = location.x + halfSize
        val minZ = location.z - halfSize
        val maxZ = location.z + halfSize

        return BoundingBox(minX, -256.0, minZ, maxX, 256.0, maxZ)
    }

    fun cellExists(id: String): Boolean {
        return cells.containsKey(id.lowercase())
    }

    fun isValidCellName(name: String): Boolean {
        return name.length in 3..16
    }

    private fun getNextPlacement(): Location {
        val lastCell = cells.values.lastOrNull() ?: return getDefaultPlacement()
        val lastCellBoundingBox = lastCell.bound
        val location = Location(
            getCellsWorld(),
            lastCellBoundingBox.centerX,
            lastCellBoundingBox.centerY,
            lastCellBoundingBox.centerZ
        )

        val addZAxis = location.x / CELLS_PER_AXIS >= MAXIMUM_CELL_LINEAR_SIZE

        if (addZAxis) {
            location.x = 0.0
            location.z += MINIMUM_CELL_LINEAR_SIZE
        } else {
            location.x += MINIMUM_CELL_LINEAR_SIZE
        }

        return location
    }

    private fun getDefaultPlacement(): Location {
        return getCellsWorld().spawnLocation
    }

    private fun getCellsWorld() = core.server.worlds.find { it.name == "cells" }!!
}