package me.outspending.core.storage.database.impl

import me.outspending.core.storage.data.CellData
import me.outspending.core.storage.database.Database

class CellDatabase : Database<String, CellData> {
    override fun createTable() {
        TODO("Not yet implemented")
    }

    override fun updateAllData() {
        TODO("Not yet implemented")
    }

    override fun getAllData(): List<CellData>? {
        TODO("Not yet implemented")
    }

    override fun hasData(key: String): Boolean {
        TODO("Not yet implemented")
    }

    override fun getData(key: String): CellData? {
        TODO("Not yet implemented")
    }

    override fun deleteData(key: String) {
        TODO("Not yet implemented")
    }

    override fun updateData(key: String, value: CellData) {
        TODO("Not yet implemented")
    }

    override fun createData(key: String, value: CellData) {
        TODO("Not yet implemented")
    }
}