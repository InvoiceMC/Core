package me.outspending.core.mining.enchants

import me.outspending.core.Utilities.regex
import me.outspending.core.Utilities.toTinyString
import me.outspending.core.helpers.FormatHelper.Companion.parse
import net.kyori.adventure.text.Component
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.persistence.PersistentDataType

object PickaxeEnchanter {

    private fun ItemMeta.getLoreMutable(): MutableList<Component>? = lore()?.toMutableList()

    private fun editEnchant(
        meta: ItemMeta,
        data: PersistentDataContainer,
        lore: MutableList<Component>,
        pickaxeEnchant: String,
        add: Int
    ) {
        val enchantKey = NamespacedKey("enchant", pickaxeEnchant)

        val enchantValue = data.getOrDefault(enchantKey, PersistentDataType.INTEGER, 0)
        val enchantLore =
            data.getOrDefault(NamespacedKey("lore", pickaxeEnchant), PersistentDataType.INTEGER, 0)

        data.set(enchantKey, PersistentDataType.INTEGER, (enchantValue + add))

        lore[enchantLore] =
            " <second><b>|</b> <gray>${pickaxeEnchant.toTinyString()}: <white>${(enchantValue + add).regex()}"
                .parse()

        meta.lore(lore)
    }

    private fun addEnchant(
        meta: ItemMeta,
        data: PersistentDataContainer,
        lore: MutableList<Component>,
        pickaxeEnchant: String,
        level: Int
    ) {
        val enchantKey = NamespacedKey("enchant", pickaxeEnchant)
        val index = lore.size - 1

        data.set(enchantKey, PersistentDataType.INTEGER, level)
        data.set(NamespacedKey("lore", pickaxeEnchant), PersistentDataType.INTEGER, index)

        lore[index] =
            " <second><b>|</b> <gray>${pickaxeEnchant.toTinyString()}: <white>${level.regex()}".parse()
        lore.add("".parse())

        meta.lore(lore)
    }

    fun enchantPickaxe(item: ItemStack, enchant: String, level: Int): ItemStack {
        item.editMeta { meta ->
            val data = meta.persistentDataContainer
            val key = NamespacedKey("enchant", enchant)
            val lore = meta.getLoreMutable() ?: return@editMeta

            if (data.has(key)) editEnchant(meta, data, lore, enchant, level)
            else addEnchant(meta, data, lore, enchant, level)
        }

        return item
    }

    fun enchantPickaxe(item: ItemStack, enchant: PickaxeEnchant, level: Int): ItemStack =
        enchantPickaxe(item, enchant.getEnchantName(), level)
}