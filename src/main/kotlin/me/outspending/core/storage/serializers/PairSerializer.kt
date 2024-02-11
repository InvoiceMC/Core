package me.outspending.core.storage.serializers

import me.outspending.munch.serializer.Serializer
import me.outspending.munch.serializer.SerializerFactory
import java.lang.reflect.ParameterizedType
import kotlin.reflect.KClass

class PairSerializer<K : Any, V : Any> : Serializer<Pair<K, V>> {
    @Suppress("UNCHECKED_CAST")
    private fun getKClass(): KClass<K> {
        val type = javaClass.genericSuperclass as ParameterizedType
        return type.actualTypeArguments[0] as KClass<K>
    }

    @Suppress("UNCHECKED_CAST")
    private fun getVClass(): KClass<V> {
        val type = javaClass.genericSuperclass as ParameterizedType
        return type.actualTypeArguments[1] as KClass<V>
    }

    private fun getKSerializer(): Serializer<K> {
        val clazz = getKClass()
        return SerializerFactory.getSerializer(clazz) ?: error("No serializer found for $clazz")
    }

    private fun getVSerializer(): Serializer<V> {
        val clazz = getVClass()
        return SerializerFactory.getSerializer(clazz) ?: error("No serializer found for $clazz")
    }

    @Suppress("UNCHECKED_CAST")
    override fun getSerializerClass(): Class<Pair<K, V>> = Pair::class.java as Class<Pair<K, V>>

    override fun deserialize(str: String): Pair<K, V> {
        val kSerializer = getKSerializer()
        val vSerializer = getVSerializer()

        val values = str.split(",")
        if (values.size != 2) throw IllegalArgumentException("Invalid pair string: $str")

        val k = kSerializer.deserialize(values[0])
        val v = vSerializer.deserialize(values[1])

        return Pair(k, v)
    }

    @Suppress("UNCHECKED_CAST")
    override fun serialize(obj: Any?): String {
        val pair = obj as? Pair<K, V> ?: return ""
        val kSerializer = getKSerializer()
        val vSerializer = getVSerializer()

        val k = kSerializer.serialize(pair.first)
        val v = vSerializer.serialize(pair.second)

        return "$k,$v"
    }
}