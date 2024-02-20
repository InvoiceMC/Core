package me.outspending.core.pmines.data

import me.outspending.core.data.DataPersistenceHandler
import me.outspending.core.data.Extensions.getData
import me.outspending.core.pmines.PrivateMine
import org.bukkit.Bukkit
import org.bukkit.entity.Player

val pmineDataManager = PmineDataManager()

class PmineDataManager {

    private val pmineData: MutableMap<String, PrivateMine> = mutableMapOf()
    private val persistenceHandler: DataPersistenceHandler<PrivateMine, String> =
        PmineDataPersistenceHandler()

    fun getPmine(name: String): PrivateMine {
        val pmine = pmineData[name]
        requireNotNull(pmine) { "The pmine named $name doesn't exist" }

        return pmine
    }

    fun addPmine(mine: PrivateMine) {
        pmineData[mine.getMineName()] = mine
    }

    fun savePmine(name: String) = savePmine(getPmine(name))

    fun savePmine(mine: PrivateMine) = persistenceHandler.save(mine.getMineName(), mine)

    fun saveAndDeletePmine(name: String) {
        val mine = getPmine(name)

        val onlinePlayers = Bukkit.getOnlinePlayers()
        val anyoneOnline = mine.getAllMembers().any { it in onlinePlayers }
        if (!anyoneOnline) { // Only save's and deletes if no one from the pmine is online
            savePmine(name)
            pmineData.remove(name)
        }
    }

    fun loadPmine(player: Player) {
        val data = player.getData()
        data.pmineName?.let { loadPmine(it) }
    }

    fun loadPmine(name: String) {
        if (pmineData.containsKey(name)) return // Means it's already loaded

        val mine = persistenceHandler.load(name)
        pmineData[name] = mine
    }

    fun getPmineNames(): Set<String> = pmineData.keys

    fun hasPmineName(name: String) = pmineData.containsKey(name)
}
