package me.outspending.core.commands.admin

import com.azuyamat.maestro.bukkit.annotations.Command
import me.outspending.core.mining.MineUtils
import me.outspending.core.mining.enchants.gui.EnchantUpgradeGUI
import me.outspending.core.mining.enchants.types.JackhammerEnchant
import me.outspending.core.mining.shapes.SphereShape
import org.bukkit.Material
import org.bukkit.entity.Player

@Command(name = "enchanttest", permission = "core.enchanttest", description = "wyd here nerd")
class EnchantTestCommand {

    fun onCommand(player: Player) {
        val heldItem = player.inventory.itemInMainHand
        require(heldItem.type == Material.DIAMOND_PICKAXE) { return }

        EnchantUpgradeGUI(player, JackhammerEnchant()).open()
    }
}
