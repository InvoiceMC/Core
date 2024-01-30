package me.outspending.core.storage.database.serializer

interface DatabaseSerializer<T> {
    fun getSerializerType(): Class<T>

    fun serialize(obj: T): String

    fun deserialize(str: String): T?
}