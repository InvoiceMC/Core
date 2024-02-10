package me.outspending.core.commands.admin

import com.azuyamat.maestro.bukkit.annotations.Command
import me.outspending.core.mining.enchants.PickaxeEnchant
import me.outspending.core.mining.pickaxe.PickaxeUpdater
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
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
            val newItem: ItemStack = PickaxeUpdater.enchantPickaxe(item, enchant, level)
            player.inventory.setItemInMainHand(newItem)
        }
    }
}
