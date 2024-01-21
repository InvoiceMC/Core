package me.outspending.core.commands

import me.outspending.core.utils.PersistentUtils
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.PlayerInventory
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.persistence.PersistentDataType

class EnchantCMD : Command("enchant") {
    override fun execute(player: CommandSender, p1: String, args: Array<out String>?): Boolean {
        if (!player.hasPermission("core.enchant")) {
            player.sendMessage("You do not have permission to use this command!")
            return false
        }

        if (player is Player) {
            val inventory: PlayerInventory = player.inventory
            val item = inventory.itemInMainHand

            if (args == null || args.isEmpty()) {
                player.sendMessage("Usage: /enchant <enchant>")
                return false
            }

            if (item.type == Material.DIAMOND_PICKAXE) {
                val itemMeta: ItemMeta = item.itemMeta
                val dataContainer: PersistentDataContainer =
                    PersistentUtils.getPersistentData(itemMeta)
                dataContainer.set(
                    NamespacedKey("enchant", args[0]),
                    PersistentDataType.INTEGER,
                    args[1].toInt()
                )

                item.setItemMeta(itemMeta)
                inventory.setItemInMainHand(item)
            }

            return true
        }

        return false
    }
}
