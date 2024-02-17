package me.outspending.core.data.serializers

import me.outspending.munch.serializer.Serializer
import org.bukkit.Bukkit
import org.bukkit.Location

class LocationSerializer : Serializer<Location> {
    override fun getSerializerClass(): Class<Location> = Location::class.java

    override fun deserialize(str: String): Location {
        val parts = str.split(",")
        require (parts.size == 6) { "Invalid Location Format" }

        val world = Bukkit.getWorld(parts[0]) ?: error("World ${parts[0]} doesn't exist")
        val x = parts[1].toDouble()
        val y = parts[2].toDouble()
        val z = parts[3].toDouble()
        val yaw = parts[4].toFloat()
        val pitch = parts[5].toFloat()

        return Location(world, x, y, z, yaw, pitch)
    }

    override fun serialize(obj: Any?): String {
        val location = obj as? Location ?: return ""

        return "${location.world.name},${location.x},${location.y},${location.z},${location.yaw},${location.pitch}"
    }
}
