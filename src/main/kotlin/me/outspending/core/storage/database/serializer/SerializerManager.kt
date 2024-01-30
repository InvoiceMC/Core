package me.outspending.core.storage.database.serializer

import java.sql.PreparedStatement
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberProperties
import me.outspending.core.storage.data.Data
import me.outspending.core.storage.data.PropertyOrder

class SerializerManager<T : Data>(private var data: T) {

    private val properties =
        data::class.memberProperties.sortedBy {
            it.findAnnotation<PropertyOrder>()?.order ?: Int.MAX_VALUE
        }

    fun serialize(
        statement: PreparedStatement,
        function: (Int, PreparedStatement) -> Unit
    ): PreparedStatement {
        for ((index, property) in properties.withIndex()) {
            val name = property.name
            val value = property.getter.call(data) ?: continue
            val type = property.returnType

            println("[$index] Setting $name to $value")

            when (type.classifier) {
                String::class -> statement.setString(index + 1, value as String)
                Int::class -> statement.setInt(index + 1, value as Int)
                Long::class -> statement.setLong(index + 1, value as Long)
                Float::class -> statement.setFloat(index + 1, value as Float)
                Double::class -> statement.setDouble(index + 1, value as Double)
                Boolean::class -> statement.setBoolean(index + 1, value as Boolean)
                else -> {
                    val serialized = Serializers.serialize(value)
                    statement.setString(index + 1, serialized)
                }
            }

            println("[$index] Set $name to $value")
        }

        function(properties.size, statement)
        return statement
    }

    fun deserialize(statement: PreparedStatement, function: (Int, PreparedStatement) -> Unit): T {
        val resultSet = statement.executeQuery()

        for ((index, property) in properties.withIndex()) {
            val name = property.name
            val type = property.returnType

            println("[$index] Setting variable $name")

            val mutableProperty = (property as? KMutableProperty<*>) ?: continue
            val setValue: (Any) -> Unit = { it -> mutableProperty.setter.call(data, it) }

            when (type.classifier) {
                String::class -> setValue(resultSet.getString(index + 1))
                Int::class -> setValue(resultSet.getInt(index + 1))
                Long::class -> setValue(resultSet.getLong(index + 1))
                Double::class -> setValue(resultSet.getDouble(index + 1))
                Float::class -> setValue(resultSet.getFloat(index + 1))
                else -> {
                    val value = resultSet.getString(index + 1)
                    val clazz = type.classifier as Class<*>? ?: continue
                    val deserialized = Serializers.deserialize(clazz, value)

                    mutableProperty.setter.call(data, deserialized)
                }
            }

            println("[$index] Set variable $name")
        }

        function(properties.size + 1, statement)
        return data
    }

    companion object {
        @Suppress("UNCHECKED_CAST")
        fun <T : Data> deserializeWithoutData(clazz: KClass<*>, statement: PreparedStatement): T {
            val resultSet = statement.executeQuery()
            val instance = clazz.createInstance() as T

            val properties =
                clazz.declaredMemberProperties.sortedBy {
                    it.findAnnotation<PropertyOrder>()?.order ?: Int.MAX_VALUE
                }

            for ((index, property) in properties.withIndex()) {
                val type = property.returnType

                val mutableProperty = (property as? KMutableProperty<*>) ?: continue
                val setValue: (Any) -> Unit = {
                    mutableProperty.setter.call(instance, it)
                    println("${mutableProperty.name} // $it // ${index}")
                }

                when (type.classifier) {
                    String::class -> setValue(resultSet.getString(index + 1))
                    Int::class -> setValue(resultSet.getInt(index + 1))
                    Long::class -> setValue(resultSet.getLong(index + 1))
                    Double::class -> setValue(resultSet.getDouble(index + 1))
                    Float::class -> setValue(resultSet.getFloat(index + 1))
                    else -> {
                        val value: String = resultSet.getString(index + 1)
                        val clazz: Class<*> = type.classifier!!::class.java

                        val deserialized = Serializers.deserialize(clazz, value)

                        mutableProperty.setter.call(instance, deserialized)
                    }
                }
            }

            return instance
        }
    }
}
