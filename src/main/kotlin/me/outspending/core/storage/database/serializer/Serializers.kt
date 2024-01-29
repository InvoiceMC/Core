package me.outspending.core.storage.database.serializer

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import org.bukkit.util.BoundingBox
import org.reflections.Reflections

object Serializers {
    val serializers: Map<Class<*>, DatabaseSerializer<*>> =
        Reflections(Serializers::class.java)
            .getTypesAnnotatedWith(SerializerType::class.java)
            .associate { clazz ->
                val constructor = clazz.constructors.firstOrNull()
                if (constructor != null) {
                    val serializerInstance = constructor.newInstance() as DatabaseSerializer<*>
                    val type = serializerInstance.getSerializerType()

                    type to serializerInstance
                } else {
                    throw Exception("No constructor found for ${clazz.simpleName}")
                }
            }

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> serialize(obj: T): String {
        val serializer = serializers[obj::class.java] as DatabaseSerializer<T>
        return serializer.serialize(obj)
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> deserialize(clazz: Class<T>, str: String): T? {
        val serializer = serializers[clazz] as DatabaseSerializer<T>
        return serializer.deserialize(str)
    }

    @SerializerType
    class ListSerializer<T : Any> : DatabaseSerializer<List<T>> {
        @Suppress("UNCHECKED_CAST")
        override fun getSerializerType(): Class<List<T>> = List::class.java as Class<List<T>>

        override fun serialize(obj: List<T>): String {
            return obj.joinToString(prefix = "[", postfix = "]", separator = ",") { it.toString() }
        }

        @Suppress("UNCHECKED_CAST")
        override fun deserialize(str: String): List<T> {
            return str.substring(1, str.length - 1).split(",").map {
                serializers[it::class.java]?.deserialize(it) as T
            }
        }
    }

    @SerializerType
    class PlayerSerializer : DatabaseSerializer<Player> {
        override fun getSerializerType(): Class<Player> = Player::class.java

        override fun serialize(obj: Player): String = obj.uniqueId.toString()

        override fun deserialize(str: String): Player? = Bukkit.getPlayer(str)
    }

    @SerializerType
    class OfflinePlayerSerializer : DatabaseSerializer<OfflinePlayer> {
        override fun getSerializerType(): Class<OfflinePlayer> = OfflinePlayer::class.java

        override fun serialize(obj: OfflinePlayer): String = obj.uniqueId.toString()

        override fun deserialize(str: String): OfflinePlayer = Bukkit.getOfflinePlayer(str)
    }

    @SerializerType
    class LocationSerializer : DatabaseSerializer<Location> {
        override fun getSerializerType(): Class<Location> = Location::class.java

        override fun serialize(obj: Location): String {
            return "${obj.world.name},${obj.x},${obj.y},${obj.z},${obj.yaw},${obj.pitch}"
        }

        override fun deserialize(str: String): Location? {
            val split = str.split(",")
            return if (split.size == 6) {
                val world = Bukkit.getWorld(split[0])
                require(world != null) { "World ${split[0]} cannot be null!" }

                Location(
                    world,
                    split[1].toDouble(),
                    split[2].toDouble(),
                    split[3].toDouble(),
                    split[4].toFloat(),
                    split[5].toFloat()
                )
            } else null
        }
    }

    @SerializerType
    class BoundingBoxSerializer : DatabaseSerializer<BoundingBox> {
        override fun getSerializerType(): Class<BoundingBox> = BoundingBox::class.java

        override fun deserialize(str: String): BoundingBox? {
            val split = str.split(",").map { it.toDouble() }
            return if (split.size == 6) {
                BoundingBox(
                    split[0],
                    split[1],
                    split[2],
                    split[3],
                    split[4],
                    split[5]
                )
            } else null
        }

        override fun serialize(obj: BoundingBox): String {
            return "${obj.minX},${obj.minY},${obj.minZ},${obj.maxX},${obj.maxY},${obj.maxZ}"
        }

    }
}
