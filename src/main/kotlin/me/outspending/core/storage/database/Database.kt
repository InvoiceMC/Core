package me.outspending.core.storage.database

interface Database<K, V> {
    fun createTable()

    fun createData(
        key: K,
        value: V,
    )

    fun updateData(
        key: K,
        value: V,
    )

    fun updateAllData()

    fun deleteData(key: K)

    fun getData(key: K): V?

    fun getAllData(): List<V>?

    fun hasData(key: K): Boolean
}
