package me.outspending.core.mining.enchants.gui

import me.outspending.core.Utilities.toUpperCase
import me.outspending.core.core
import me.outspending.core.mining.enchants.EnchantHandler
import me.outspending.core.misc.helpers.FormatHelper.Companion.parse
import me.tech.mcchestui.GUI
import me.tech.mcchestui.GUIType
import me.tech.mcchestui.item.item
import me.tech.mcchestui.utils.gui
import me.tech.mcchestui.utils.openGUI
import org.bukkit.Material
import org.bukkit.entity.Player

class EnchantGUI(private val player: Player) {

    private fun createGUI(): GUI {
        return gui(
            plugin = core,
            title = "Upgrade Pickaxe".parse(),
            type = GUIType.Chest(5)
        ) {
            fillBorder { item = item(Material.GRAY_STAINED_GLASS_PANE) { name = " ".parse() } }

            var y = 2
            var x = 2
            for (enchant in EnchantHandler.pickaxeEnchants) {
                val enchantName: String = enchant.getEnchantName()
                val enchantItem: Material = enchant.getEnchantItem()

                slot(x, y) {
                    item = item(enchantItem) {
                        name = "<main>${enchantName.toUpperCase()}".parse()
                        lore = listOf(
                            "",
                            "<green>Click to upgrade this enchant."
                        ).map { it.parse() }
                        onClick {
                            EnchantUpgradeGUI(player, enchant).open()
                        }
                    }
                }

                x++
                if (x > 8) {
                    x = 2
                    y++
                }
            }
        }
    }

    fun open() {
        val gui = createGUI()
        player.openGUI(gui)
    }
}