package me.outspending.core.commands

import me.outspending.core.utils.Utilities.toComponent
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack

class PickaxeCMD : Command("pickaxe") {
    override fun execute(player: CommandSender, p1: String, p2: Array<out String>?): Boolean {
        if (player is Player) {
            val itemStack = ItemStack(Material.DIAMOND_PICKAXE)
            itemStack.editMeta() { meta ->
                meta.displayName(
                    "<gradient:#e37622:#deb799>${player.name}'s</gradient> <gray>Pickaxe"
                        .toComponent()
                )
                meta.lore(
                    listOf(
                            "<dark_gray>Owned by ${player.name}",
                            "",
                            "<#ff3333>ɪɴꜰᴏʀᴍᴀᴛɪᴏɴ<r>",
                            "<#ff3333><b>|</b> N/A",
                            "<#ff3333><b>|</b> N/A",
                            "<#ff3333><b>|</b> N/A",
                            "",
                        )
                        .map { it.toComponent() }
                )

                meta.addEnchant(Enchantment.DIG_SPEED, 1000, true)
                meta.isUnbreakable = true

                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE)
            }

            player.inventory.addItem(itemStack)
            return true
        }

        return false
    }
}
