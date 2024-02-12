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

class MainEnchantGUI(private val player: Player): EnchantGUI {

    override fun createGUI(): GUI {
        val heldItem = player.inventory.itemInMainHand
        val meta = heldItem.itemMeta
        val data = meta.persistentDataContainer
        return gui(
            plugin = core,
            title = "Upgrade Pickaxe".parse(),
            type = GUIType.Chest(5)
        ) {
            fillBorder { item = item(Material.GRAY_STAINED_GLASS_PANE) { name = " ".parse() } }

            var y = 2
            var x = 2
            for (enchant in EnchantHandler.pickaxeEnchants) {
                val enchantLevel = enchant.getEnchantmentLevel(data)
                val maxLevel = enchant.getMaxEnchantmentLevel()
                val chance = if (enchant.getEnchantmentChance(enchantLevel) <= 100) enchant.getEnchantmentChance(enchantLevel) else 100
                val enchantName: String = enchant.getEnchantName()
                val enchantItem: Material = enchant.getEnchantItem()

                slot(x, y) {
                    item = item(enchantItem) {
                        name = "<main>${enchantName.toUpperCase()}".parse()
                        lore = listOf(
                            "<dark_gray><i>ᴇɴᴄʜᴀɴᴛ",
                            "",
                            "<second>${enchant.getDescription()}",
                            "<second>${enchantLevel}<gray>/<second>${maxLevel} <gray>($chance%)",
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

    override fun open() {
        val gui = createGUI()
        player.openGUI(gui)
    }
}