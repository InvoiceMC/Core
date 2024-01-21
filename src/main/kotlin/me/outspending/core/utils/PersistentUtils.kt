package me.outspending.core.utils

import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.persistence.PersistentDataContainer

object PersistentUtils {

    fun getPersistentData(item: ItemStack): PersistentDataContainer =
        getPersistentData(item.itemMeta)

    fun getPersistentData(itemMeta: ItemMeta): PersistentDataContainer =
        itemMeta.persistentDataContainer

    fun updateItem(item: ItemStack, itemMeta: ItemMeta) {
        item.itemMeta = itemMeta
    }
}
