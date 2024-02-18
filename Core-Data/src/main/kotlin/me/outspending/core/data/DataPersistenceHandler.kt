package me.outspending.core.data

interface DataPersistenceHandler<K, V> {

    fun save(value: V, data: K?)

    fun load(value: V): K

}