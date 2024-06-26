package me.outspending.core.mining.enchants.gui

import me.outspending.core.CoreHandler.core
import me.outspending.core.helpers.FormatHelper.Companion.parse
import me.outspending.core.mining.enchants.EnchantHandler
import me.outspending.core.toUpperCase
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
                val enchantLevel = enchant.getEnchantmentLevel(data) ?: 0
                val maxLevel = enchant.getMaxEnchantmentLevel()
                val chance = if (enchant.getEnchantmentChance(enchantLevel) <= 100) enchant.getEnchantmentChance(enchantLevel) else 100
                val enchantName: String = enchant.getEnchantName()
                val enchantItem: Material = enchant.getEnchantItem()

                slot(x, y) {
                    item = item(enchantItem) {
                        name = "<main>${enchantName.toUpperCase()}".parse()
                        lore = listOf(
                            "<dark_gray>ᴇɴᴄʜᴀɴᴛ",
                            "",
                            "<main><b>ɪɴꜰᴏʀᴍᴀᴛɪᴏɴ</b></main>",
                            " <second><b>|</b> <white>${enchant.getDescription()}",
                            "",
                            "<main><b>ᴇɴᴄʜᴀɴᴛ</b></main>",
                            " <second><b>|</b> <white>Level: <main>${enchantLevel}<gray>/<second>${maxLevel}",
                            " <second><b>|</b> <white>Chance: <main>${"%.4f".format(chance)}%",
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