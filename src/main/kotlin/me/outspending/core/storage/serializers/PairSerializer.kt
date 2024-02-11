package me.outspending.core.storage.serializers

import me.outspending.core.storage.enums.CellRank
import me.outspending.munch.serializer.Serializer
import java.util.UUID

class PairSerializer: Serializer<Pair<CellRank, UUID>> {
    override fun getSerializerClass(): Class<Pair<CellRank, UUID>> = Pair::class.java as Class<Pair<CellRank, UUID>>

    override fun deserialize(str: String): Pair<CellRank, UUID> {
        val split = str.split(":")

        return Pair(CellRank.valueOf(split[0]), UUID.fromString(split[1]))
    }

    override fun serialize(obj: Any?): String {
        val pair = obj as? Pair<CellRank, UUID> ?: return ""

        return "${pair.first.name}:${pair.second}"
    }
}