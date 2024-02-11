package me.outspending.core.storage.serializers

import me.outspending.munch.serializer.Serializer

class ListSerializer: Serializer<List<Any>> {
    override fun getSerializerClass(): Class<List<Any>> = List::class.java as Class<List<Any>>

    override fun deserialize(str: String): List<Any> {
        return str.split(",").map { it }
    }

    override fun serialize(obj: Any?): String {
        val list = obj as? List<Any> ?: return ""

        return list.joinToString(",")
    }
}