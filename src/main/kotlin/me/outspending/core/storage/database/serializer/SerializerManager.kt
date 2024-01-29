package me.outspending.core.storage.database.serializer

import me.outspending.core.storage.data.Data
import java.sql.PreparedStatement
import java.sql.Statement
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.memberProperties

class SerializerManager<T: Data>(
    private var data: T
) {

    private val properties = data::class.memberProperties

    fun serialize(statement: PreparedStatement): PreparedStatement {
        for ((index, property) in properties.withIndex()) {
            val name = property.name
            val value = property.getter.call(data)

            statement.setString(index + 1, value.toString())
            println("$index: $name = $value")
        }

        return statement
    }

    fun deserialize(statement: PreparedStatement): T {
        val resultSet = statement.executeQuery()

        for ((index, property) in properties.withIndex()) {
            val name = property.name
            val value = resultSet.getString(index + 1)

            val mutableProperty = (property as? KMutableProperty<*>) ?: continue
            mutableProperty.setter.call(data, value)

            println("$index: $name = $value")
        }

        return data
    }
}