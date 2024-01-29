package me.outspending.core.storage.database.serializer

import me.outspending.core.storage.data.Data
import java.sql.PreparedStatement
import java.sql.Statement
import kotlin.reflect.KClassifier
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KType
import kotlin.reflect.full.memberProperties

class SerializerManager<T: Data>(
    private var data: T
) {

    private val properties = data::class.memberProperties

    fun serialize(statement: PreparedStatement): PreparedStatement {
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

        return statement
    }

    fun deserialize(statement: PreparedStatement): T {
        val resultSet = statement.executeQuery()

        for ((index, property) in properties.withIndex()) {
            val name = property.name
            val type = property.returnType

            println("[$index] Setting variable $name")

            val mutableProperty = (property as? KMutableProperty<*>) ?: continue
            val setValue: (Any) -> Unit = {
                it -> mutableProperty.setter.call(data, it)
            }
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

        return data
    }
}