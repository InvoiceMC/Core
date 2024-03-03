package me.outspending.core.pmines.data

import com.github.shynixn.mccoroutine.bukkit.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import me.outspending.core.CoreHandler.core
import me.outspending.core.data.DataManager
import me.outspending.core.data.DataPersistenceHandler
import me.outspending.core.data.database
import me.outspending.core.pmines.PrivateMine

val pmineDataManager = PmineDataManager()

class PmineDataManager : DataManager<String, PrivateMine>() {
    private val persistenceHandler: DataPersistenceHandler<StorablePmine, String> =
        PmineDataPersistenceHandler()

    override fun load() {
        database.createTable(pmineMunchClass)
    }

    override fun unload() {
        saveAllData()
    }

    override fun saveAllData() {
        core.launch {
            val allData = getAllDataList().map { it.toStorable() }
            async { database.updateAllData(pmineMunchClass, allData) }.await()
        }
    }

    override fun getData(key: String): PrivateMine = data[key]!!

    override fun saveData(key: String) {
        if (!data.containsKey(key)) return

        val mine = getData(key)
        core.launch {
            async(Dispatchers.IO) { persistenceHandler.save(key, mine.toStorable()) }.await()
        }
    }

    override fun unloadData(key: String) {
        if (!data.containsKey(key)) return

        val pmine = data.remove(key)
        pmine?.let {
            core.launch {
                async(Dispatchers.IO) { persistenceHandler.save(key, it.toStorable()) }.await()
            }
        }
    }

    override fun loadData(key: String) {
        if (data.containsKey(key)) return

        core.launch {
            data[key] = async(Dispatchers.IO) { persistenceHandler.load(key) }.await().toPmine()
        }
    }
}
