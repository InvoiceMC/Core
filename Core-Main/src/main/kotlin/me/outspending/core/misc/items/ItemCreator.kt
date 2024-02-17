package me.outspending.core.misc.items

import me.outspending.core.helpers.FormatHelper.Companion.parse
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class ItemCreator @JvmOverloads constructor(var material: Material, amount: Int = 1) {
    var amount: Int = 1
        private set
    private var name: Component? = null
    private var lore: List<Component>? = null


    init {
        this.amount = amount
    }

    fun material(material: Material): ItemCreator {
        this.material = material
        return this
    }

    fun name(name: String): ItemCreator {
        this.name = name.parse()
        return this
    }

    fun amount(amount: Int): ItemCreator {
        this.amount = amount
        return this
    }

    fun lore(lore: List<String>): ItemCreator {
        this.lore = lore.map { it.parse() }
        return this
    }

    fun create(): ItemStack {
        val item = ItemStack(this.material, this.amount)
        val meta = item.itemMeta
        if (name != null) {
            meta.displayName(this.name)
        }
        if (lore != null) {
            meta.lore(this.lore)
        }
        item.setItemMeta(meta)

        return item
    }
}
