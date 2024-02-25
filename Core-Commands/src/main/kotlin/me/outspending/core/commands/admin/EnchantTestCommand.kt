package me.outspending.core.commands.admin

import com.azuyamat.maestro.common.annotations.Command
import org.bukkit.Material
import org.bukkit.entity.Player

@Command(name = "enchanttest", permission = "core.enchanttest", description = "wyd here nerd")
class EnchantTestCommand {

    fun onCommand(player: Player) {
        val heldItem = player.inventory.itemInMainHand
        require(heldItem.type == Material.DIAMOND_PICKAXE) { return }
    }
}
