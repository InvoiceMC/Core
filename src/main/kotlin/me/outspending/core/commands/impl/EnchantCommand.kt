package me.outspending.core.commands.impl

import me.outspending.core.commands.annotations.Command
import me.outspending.core.utils.PersistentUtils
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.inventory.PlayerInventory
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.persistence.PersistentDataType

@Command(
    name = "enchant",
    permission = "core.enchant",
)
class EnchantCommand {

    fun onCommand(player: Player, enchant: String, level: Int) {
        val inventory: PlayerInventory = player.inventory
        val item = inventory.itemInMainHand

        if (item.type == Material.DIAMOND_PICKAXE) {
            val itemMeta: ItemMeta = item.itemMeta
            val dataContainer: PersistentDataContainer =
                PersistentUtils.getPersistentData(itemMeta)
            dataContainer.set(
                NamespacedKey("enchant", enchant),
                PersistentDataType.INTEGER,
                level
            )

            item.setItemMeta(itemMeta)
            inventory.setItemInMainHand(item)
        }
    }
}
