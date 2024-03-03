package me.outspending.core.data

abstract class DataManager<K : Any, V : Any>(val data: MutableMap<K, V>) {
    constructor() : this(mutableMapOf())

    abstract fun load()
    abstract fun unload()
    abstract fun saveAllData()
    abstract suspend fun loadData(key: K): V
    abstract fun unloadData(key: K)
    abstract fun saveData(key: K)
    abstract fun getData(key: K): V

    fun clear() = data.clear()

    fun hasData(key: K) = data.containsKey(key)

    fun addData(key: K, value: V) = data.put(key, value)

    fun removeData(key: K) = data.remove(key)

    fun getAllDataList() : List<V> = data.values.toList()

    fun getAllDataMap() : Map<K, V> = data
}