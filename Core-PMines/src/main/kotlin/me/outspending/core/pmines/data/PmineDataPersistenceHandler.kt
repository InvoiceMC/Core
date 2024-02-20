package me.outspending.core.pmines.data

import me.outspending.core.data.DataPersistenceHandler
import me.outspending.core.pmines.PrivateMine

class PmineDataPersistenceHandler : DataPersistenceHandler<PrivateMine, String> {
    override fun save(value: String, data: PrivateMine?) {
        println("Saving pmine $value")
    }

    override fun load(value: String): PrivateMine {
        TODO()
    }
}
