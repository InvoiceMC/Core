package me.outspending.core.mining

import me.outspending.core.format
import me.outspending.core.helpers.FormatHelper.Companion.parse
import net.kyori.adventure.text.Component
import org.bukkit.NamespacedKey
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.persistence.PersistentDataType

object PickaxeUpdater {
    private val brokenKey = NamespacedKey("stats", "blocks_broken")

    private fun PersistentDataType<Int, Int>.add(
        container: PersistentDataContainer,
        key: NamespacedKey,
        value: Int
    ) {
        val current = container.getOrDefault(key, this, 0)
        container.set(key, this, current + value)
    }

    fun updatePickaxeInformation(meta: ItemMeta, amount: Int): ItemMeta {
        val data = meta.persistentDataContainer
        val lore: MutableList<Component> = (meta.lore() ?: return meta).toMutableList()

        PersistentDataType.INTEGER.add(data, brokenKey, amount)

        val blocksBroken = data.get(brokenKey, PersistentDataType.INTEGER)!!
        lore[4] = " <second><b>|</b> <gray>ʙʟᴏᴄᴋꜱ ʙʀᴏᴋᴇɴ: <white>${blocksBroken.format()}".parse()

        meta.lore(lore)
        return meta
    }
}
