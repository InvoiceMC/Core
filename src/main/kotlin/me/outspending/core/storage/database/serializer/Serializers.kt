package me.outspending.core.storage.database.serializer

import me.outspending.core.storage.database.Database
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import org.reflections.Reflections
import java.lang.reflect.ParameterizedType

class Serializers {
    companion object {
        val serializers: Map<Class<*>, DatabaseSerializer<*>> = Reflections(Serializers::class.java)
            .getTypesAnnotatedWith(SerializerType::class.java)
            .associate { clazz ->
                val serializerInstance = clazz.getDeclaredConstructor().newInstance() as DatabaseSerializer<*>
                val genericType = (clazz.genericInterfaces[0] as ParameterizedType).actualTypeArguments[0] as Class<*>

                genericType to serializerInstance
            }
    }

    @SerializerType
    inner class ListSerializer<T : Any> : DatabaseSerializer<List<T>> {
        override fun serialize(obj: List<T>): String {
            return obj.joinToString(prefix = "[", postfix = "]", separator = ",") { it.toString() }
        }

        @Suppress("UNCHECKED_CAST")
        override fun deserialize(str: String): List<T> {
            return str.substring(1, str.length - 1)
                .split(",")
                .map { serializers[it::class.java]?.deserialize(it) as T }
        }
    }

    @SerializerType
    inner class PlayerSerializer : DatabaseSerializer<Player> {
        override fun serialize(obj: Player): String = obj.uniqueId.toString()

        override fun deserialize(str: String): Player? = Bukkit.getPlayer(str)
    }

    @SerializerType
    inner class OfflinePlayerSerializer : DatabaseSerializer<OfflinePlayer> {
        override fun serialize(obj: OfflinePlayer): String = obj.uniqueId.toString()

        override fun deserialize(str: String): OfflinePlayer = Bukkit.getOfflinePlayer(str)
    }

    @SerializerType
    inner class LocationSerializer : DatabaseSerializer<Location> {
        override fun serialize(obj: Location): String {
            return "${obj.world.name},${obj.x},${obj.y},${obj.z},${obj.yaw},${obj.pitch}"
        }

        override fun deserialize(str: String): Location? {
            val split = str.split(",")
            return Location(
                Bukkit.getWorld(split[0]),
                split[1].toDouble(),
                split[2].toDouble(),
                split[3].toDouble(),
                split[4].toFloat(),
                split[5].toFloat()
            )
        }

    }
}
