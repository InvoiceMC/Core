package me.outspending.core.creator

import me.outspending.core.helpers.FormatHelper.Companion.parse
import me.outspending.core.toTinyString
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack

class ItemCreator(
    private var material: Material
) : Creator<ItemStack> {
    private var name: Component? = null
    private var type: String? = null
    private var lore: List<Component>? = null
    private var flags: Array<out ItemFlag>? = null

    fun name(name: String) = apply { this.name = name.parse() }
    fun name(name: Component) = apply { this.name = name }

    fun type(type: String) = apply { this.type = type }

    fun lore(vararg lore: String) = apply { this.lore = lore.map { it.parse() } }
    fun lore(vararg lore: Component) = apply { this.lore = lore.toList() }

    fun flags(vararg flags: ItemFlag) = apply { this.flags = flags }

    fun material(material: Material) = apply { this.material = material }

    override fun build(): ItemStack {
        val itemStack = ItemStack(material)
        itemStack.editMeta { meta ->
            name?.let { meta.displayName(it) }
            lore?.let { lore ->
                val mutableLore = lore.toMutableList()
                this.type?.let { type ->
                    mutableLore.add(0, "<dark_gray>${type.toTinyString()}".parse())
                    mutableLore.add(1, "".parse())
                }

                meta.lore(mutableLore)
            }
            flags?.let { meta.addItemFlags(*it) }
        }

        return itemStack
    }
}