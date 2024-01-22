package me.outspending.core.storage.database

import me.outspending.core.mine.PrivateMine
import java.util.*

class PmineDatabase : Database<UUID, PrivateMine> {
    override fun createTable() {
        TODO("Not yet implemented")
    }

    override fun updateAllData() {
        TODO("Not yet implemented")
    }

    override fun getAllData(): List<PrivateMine>? {
        TODO("Not yet implemented")
    }

    override fun hasData(key: UUID): Boolean {
        TODO("Not yet implemented")
    }

    override fun getData(key: UUID): PrivateMine? {
        TODO("Not yet implemented")
    }

    override fun deleteData(key: UUID) {
        TODO("Not yet implemented")
    }

    override fun updateData(key: UUID, value: PrivateMine) {
        TODO("Not yet implemented")
    }

    override fun createData(key: UUID, value: PrivateMine) {
        TODO("Not yet implemented")
    }
}