package me.outspending.core.pmines.data

import me.outspending.core.data.DataPersistenceHandler
import me.outspending.core.data.database
import me.outspending.munch.Munch

internal val pmineMunchClass = Munch.create(StorablePmine::class).process<String>()

class PmineDataPersistenceHandler : DataPersistenceHandler<StorablePmine, String> {
    override fun save(value: String, data: StorablePmine?) {
        requireNotNull(data) { "Pmine data for $value is null" }

        val hasData = database.hasData(pmineMunchClass, value) ?: false

        if (hasData) database.updateData(pmineMunchClass, data, value)
        else database.addData(pmineMunchClass, data)
    }

    override fun load(value: String): StorablePmine {
        val storablePmine = database.getData(pmineMunchClass, value)
        requireNotNull(storablePmine) { "Pmine with name $value doesn't exist" }

        return storablePmine
    }
}
