package me.outspending.core.pmines.data

import me.outspending.core.data.DataPersistenceHandler
import me.outspending.core.pmines.PrivateMine

val pmineDataManager = PmineDataManager()

class PmineDataManager {

    private val pmineData: MutableMap<String, PrivateMine> = mutableMapOf()
    private val persistenceHandler: DataPersistenceHandler<PrivateMine, String> = PmineDataPersistenceHandler()

    fun createPmineData(id: String, pmineData: PrivateMine) {
        this.pmineData[id] = pmineData
    }

    fun loadPmineData(id: String) {
        val pmineData: PrivateMine = persistenceHandler.load(id)
        this.pmineData[id] = pmineData
    }

    fun unloadPmineData(id: String) {
        val pmineData: PrivateMine? = pmineData.remove(id)
        require(pmineData != null) { "Pmine data for $id is null" }

        persistenceHandler.save(id, pmineData)
    }

    fun savePmineData(id: String) {
        val pmineData: PrivateMine = getPmineData(id)
        persistenceHandler.save(id, pmineData)
    }

    fun getPmineData(id: String): PrivateMine =
        pmineData[id] ?: throw IllegalArgumentException("Pmine data for $id is null")

    fun getPmineDataList(): List<PrivateMine> = pmineData.values.toList()

    fun getAllPmineData(): Map<String, PrivateMine> = pmineData

}