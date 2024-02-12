package me.outspending.core.storage.serializers

import me.outspending.munch.serializer.Serializer
import me.outspending.munch.serializer.SerializerFactory
import java.lang.reflect.ParameterizedType
import kotlin.reflect.KClass

class ListSerializer<T : Any> : Serializer<List<T>> {
    @Suppress("UNCHECKED_CAST")
    private fun getTClass(): KClass<T> {
        return (
            (javaClass.genericSuperclass as ParameterizedType)
                .actualTypeArguments[0] as Class<T>
        ).kotlin
    }

    private fun getSerializer(): Serializer<T> {
        val clazz = getTClass()
        return SerializerFactory.getSerializer(clazz) ?: error("No serializer found for $clazz")
    }

    @Suppress("UNCHECKED_CAST")
    override fun getSerializerClass(): Class<List<T>> = List::class.java as Class<List<T>>

    override fun deserialize(str: String): List<T> {
        val serializer = getSerializer()

        val values = str.split(",")
        return values.map { serializer.deserialize(it) }
    }

    @Suppress("UNCHECKED_CAST")
    override fun serialize(obj: Any?): String {
        val list = obj as? List<T> ?: return ""
        val serializer = getSerializer()

        val values = list.map { serializer.serialize(it) }
        return values.joinToString(",")
    }
}
