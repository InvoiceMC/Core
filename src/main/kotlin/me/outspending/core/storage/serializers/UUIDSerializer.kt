package me.outspending.core.storage.serializers

import me.outspending.munch.serializer.Serializer
import java.util.*

class UUIDSerializer : Serializer<UUID> {
    override fun getSerializerClass(): Class<UUID> = UUID::class.java

    override fun deserialize(str: String): UUID = UUID.fromString(str)

    override fun serialize(obj: Any?): String = (obj as UUID).toString()
}