package me.outspending.core.storage.serializers

import me.outspending.core.storage.enums.CellRank
import me.outspending.munch.serializer.Serializer

class CellRankSerializer: Serializer<CellRank> {
    override fun getSerializerClass(): Class<CellRank> = CellRank::class.java

    override fun deserialize(str: String): CellRank = CellRank.valueOf(str)

    override fun serialize(obj: Any?): String {
        val cellRank = obj as? CellRank ?: return ""

        return cellRank.name
    }
}