package me.outspending.core.storage.serializers

import me.outspending.munch.serializer.Serializer
import org.bukkit.Location
import org.bukkit.util.BoundingBox

class BoundingBoxSerializer : Serializer<BoundingBox> {
    private val locationSerializer = LocationSerializer()

    override fun getSerializerClass(): Class<BoundingBox> = BoundingBox::class.java

    override fun deserialize(str: String): BoundingBox {
        val parts: List<String> = str.split(":")
        require(parts.size == 2) { "Invalid BoundingBox Format" }

        val point1: Location = locationSerializer.deserialize(parts[0])
        val point2: Location = locationSerializer.deserialize(parts[1])

        return BoundingBox.of(point1, point2)
    }

    override fun serialize(obj: Any?): String {
        val boundingBox: BoundingBox = obj as? BoundingBox ?: return ""

        val point1: String = locationSerializer.serialize(boundingBox.min)
        val point2: String = locationSerializer.serialize(boundingBox.max)

        return "${point1}:${point2}"
    }
}
