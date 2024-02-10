package me.outspending.core.mining.enchants.gui

import me.outspending.core.Utilities.format
import me.outspending.core.Utilities.getData
import me.outspending.core.Utilities.toUpperCase
import me.outspending.core.core
import me.outspending.core.mining.enchants.PickaxeEnchant
import me.outspending.core.mining.enchants.PickaxeEnchanter
import me.outspending.core.misc.helpers.FormatHelper.Companion.parse
import me.tech.mcchestui.GUI
import me.tech.mcchestui.GUIType
import me.tech.mcchestui.item.item
import me.tech.mcchestui.utils.gui
import me.tech.mcchestui.utils.openGUI
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.persistence.PersistentDataType

private val ENCHANT_STEPS = arrayOf(1, 5, 25, 50, 100, 500, 1000)

class EnchantUpgradeGUI(
    private val player: Player,
    private val enchant: PickaxeEnchant
) {
    private fun getEnchantCost(currentLevel: Int, increaseLevel: Int): Double {
        val increase = enchant.getIncreaseProgression() / 100
        var cost = enchant.getInitialCost()

        for (level in currentLevel until increaseLevel) {
            cost *= (1 + increase)
        }

        return cost
    }

    private fun createGUI(): GUI {
        return gui(
            plugin = core,
            title = "Upgrade ${enchant.getEnchantName().toUpperCase()}".parse(),
            type = GUIType.Chest(3)
        ) {
            val heldItem = player.inventory.itemInMainHand
            val meta = heldItem.itemMeta
            val playerData = player.getData() ?: return@gui
            val data = meta.persistentDataContainer

            val enchantKey = NamespacedKey("enchant", enchant.getEnchantName())
            val enchantLevel = data.getOrDefault(enchantKey, PersistentDataType.INTEGER, 0)

            var index = 2
            for (num in ENCHANT_STEPS) {
                val enchantCost: Double = getEnchantCost((enchantLevel + 1), num)
                println("Amount: $enchantCost || Level: $enchantLevel || Num: $num")

                val hasEnough = enchantCost <= playerData.gold
                val material = if (hasEnough) Material.LIME_STAINED_GLASS_PANE else Material.RED_STAINED_GLASS_PANE

                slot(index, 2) {
                    item = item(material) {
                        name = "<green><u>+$num".parse()
                        lore = listOf(
                            "",
                            "<main>ɪɴꜰᴏʀᴍᴀᴛɪᴏɴ",
                            "<main><b>|</b> <gray>ᴄᴜʀʀᴇɴᴛ ʟᴇᴠᴇʟ: <white>${enchantLevel}",
                            "<main><b>|</b> <gray>ᴄᴏꜱᴛ: <yellow>⛁${enchantCost.format()}",
                            "",
                            if (hasEnough) "<green><i>Click to upgrade" else "<red><i>Not Enough Gold!"
                        ).map { it.parse() }
                        onClick { event ->
                            if (!hasEnough) {
                                val neededGold = enchantCost - playerData.gold
                                player.sendMessage("<red>You need <yellow>⛁${neededGold.format()} <red>more gold to upgrade this enchant!".parse())
                            } else {
                                playerData.gold -= enchantCost

                                val item = PickaxeEnchanter.enchantPickaxe(heldItem, enchant, num)
                                player.inventory.setItemInMainHand(item)

                                refresh()
                            }
                        }
                    }

                }

                index++
            }
        }
    }

    fun open() {
        val gui = createGUI()
        player.openGUI(gui)
    }
}
