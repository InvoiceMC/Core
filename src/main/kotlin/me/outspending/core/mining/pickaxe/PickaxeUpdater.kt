package me.outspending.core.mining.pickaxe

import me.outspending.core.Utilities.format
import me.outspending.core.Utilities.toTinyString
import me.outspending.core.Utilities.toUpperCase
import me.outspending.core.mining.enchants.PickaxeEnchant
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

    private fun ItemMeta.getLoreMutable(): MutableList<Component>? = lore()?.toMutableList()

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
            "<main><b>|</b> <gray>${pickaxeEnchant.toTinyString()}: <white>${enchantValue + add}"
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
            "<main><b>|</b> <gray>${pickaxeEnchant.toTinyString()}: <white>${level}".parse()
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
