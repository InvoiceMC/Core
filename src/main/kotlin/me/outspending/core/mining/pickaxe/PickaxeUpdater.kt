package me.outspending.core.mining.pickaxe

import me.outspending.core.Utilities.format
import me.outspending.core.misc.helpers.FormatHelper.Companion.parse
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.persistence.PersistentDataType

object PickaxeUpdater {
    private val updateKey = NamespacedKey("update", "blocks")
    private val brokenKey = NamespacedKey("stats", "blocks_broken")

    private fun PersistentDataType<Int, Int>.add(
        container: PersistentDataContainer,
        key: NamespacedKey,
        value: Int
    ) {
        val current = container.getOrDefault(key, this, 0)
        container.set(key, this, current + value)
    }

    private fun isPickaxe(item: ItemStack): Boolean = item.type == Material.DIAMOND_PICKAXE

    private fun pickaxeCanUpdate(data: PersistentDataContainer): Boolean =
        data.get(updateKey, PersistentDataType.INTEGER)!! >= 23

    private fun updatePickaxeInformation(meta: ItemMeta, data: PersistentDataContainer) {
        val lore: MutableList<Component> = (meta.lore() ?: return).toMutableList()

        val blocksBroken = data.getOrDefault(brokenKey, PersistentDataType.INTEGER, 0)
        lore[3] = "<main><b>|</b> <gray>ʙʟᴏᴄᴋꜱ ʙʀᴏᴋᴇɴ: <white>${blocksBroken.format()}".parse()

        data.set(updateKey, PersistentDataType.INTEGER, 0)

        meta.lore(lore)
    }

    fun updatePickaxe(item: ItemStack, addBB: Int): ItemStack {
        item.editMeta { meta ->
            val data = meta.persistentDataContainer

            PersistentDataType.INTEGER.add(data, updateKey, 1)
            PersistentDataType.INTEGER.add(data, brokenKey, addBB + 1)

            if (pickaxeCanUpdate(data)) {
                updatePickaxeInformation(meta, data)
            }
        }

        return item
    }
}
