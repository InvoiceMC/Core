package me.outspending.core.mining.enchants.gui

import me.outspending.core.Utilities.format
import me.outspending.core.Utilities.getData
import me.outspending.core.Utilities.toUpperCase
import me.outspending.core.core
import me.outspending.core.mining.enchants.PickaxeEnchant
import me.outspending.core.mining.enchants.PickaxeEnchanter
import me.outspending.core.misc.helpers.FormatHelper.Companion.parse
import me.outspending.core.storage.data.PlayerData
import me.tech.mcchestui.GUI
import me.tech.mcchestui.GUIType
import me.tech.mcchestui.item.item
import me.tech.mcchestui.utils.gui
import me.tech.mcchestui.utils.openGUI
import org.bukkit.Material
import org.bukkit.entity.Player
import kotlin.math.pow

private val ENCHANT_STEPS = arrayOf(1, 5, 25, 50, 100, 500) // Last one is for upgrade max
private val ENCHANT_MAX_MAX_LEVEL = 25000

class EnchantUpgradeGUI(private val player: Player, private val enchant: PickaxeEnchant) {
    private fun getEnchantCost(currentLevel: Int, increaseLevel: Int): Double {
        val increase: Double = enchant.getIncreaseProgression() / 100
        val initialCost: Double = enchant.getInitialCost()
        val totalLevel: Double = (currentLevel + increaseLevel).toDouble()

        return initialCost * (1 + increase).pow(totalLevel)
    }

    private fun getEnchantMax(playerData: PlayerData, currentLevel: Int): Pair<Double, Int> {
        var left = currentLevel
        var right = ENCHANT_MAX_MAX_LEVEL
        var cost = 0.0
        var maxLevel = currentLevel
        val enchantMax = enchant.getMaxEnchantmentLevel()

        if (currentLevel >= enchantMax) return (0.0 to currentLevel)
        while (left <= right) {
            val mid = left + (right - left) / 2
            val tempCost = getEnchantCost(currentLevel, mid - currentLevel)

            if (tempCost <= playerData.gold && mid <= enchantMax) {
                cost = tempCost
                maxLevel = mid
                left = mid + 1
            } else {
                right = mid - 1
            }
        }

        return (cost to maxLevel)
    }

    private fun createGUI(): GUI {
        return gui(
            plugin = core,
            title = "Upgrade ${enchant.getEnchantName().toUpperCase()}".parse(),
            type = GUIType.Chest(3)
        ) {
            fillBorder { item = item(Material.GRAY_STAINED_GLASS_PANE) { name = " ".parse() } }

            val heldItem = player.inventory.itemInMainHand
            val meta = heldItem.itemMeta
            val playerData = player.getData() ?: return@gui
            val data = meta.persistentDataContainer

            val enchantLevel = enchant.getEnchantmentLevel(data)

            var index = 2
            for (num in ENCHANT_STEPS) {
                val enchantCost: Double = getEnchantCost(enchantLevel, num)

                val isMaxed = enchant.getMaxEnchantmentLevel() <= (enchantLevel + num)
                val hasEnough = enchantCost <= playerData.gold

                val material =
                    if (hasEnough && !isMaxed) Material.LIME_DYE
                    else Material.RED_DYE

                slot(index, 2) {
                    item =
                        item(material) {
                            name = "<green><u>+$num".parse()
                            lore =
                                if (isMaxed) {
                                    listOf(
                                        "",
                                        "<red>You cannot upgrade this many levels!",
                                        "<red>It's already at the maximum level!",
                                        ""
                                    ).map { it.parse() }
                                } else {
                                    listOf(
                                        "",
                                        "<main>ɪɴꜰᴏʀᴍᴀᴛɪᴏɴ",
                                        "<main><b>|</b> <gray>ᴄᴜʀʀᴇɴᴛ ʟᴇᴠᴇʟ: <white>${enchantLevel}",
                                        "<main><b>|</b> <gray>ᴄᴏꜱᴛ: <yellow>⛁${enchantCost.format()}",
                                        "",
                                        if (hasEnough) "<green><i>Click to upgrade"
                                        else "<red><i>Not Enough Gold!"
                                    ).map { it.parse() }
                                }
                            onClick { event ->
                                if (isMaxed) {
                                    player.sendMessage(
                                        "<red>You cannot upgrade this many levels! It's already at the maximum level!"
                                            .parse()
                                    )
                                    return@onClick
                                }

                                if (!hasEnough) {
                                    val neededGold = enchantCost - playerData.gold
                                    player.sendMessage(
                                        "<red>You need <yellow>⛁${neededGold.format()} <red>more gold to upgrade this enchant!"
                                            .parse()
                                    )
                                } else {
                                    playerData.gold -= enchantCost

                                    val item =
                                        PickaxeEnchanter.enchantPickaxe(heldItem, enchant, num)
                                    player.inventory.setItemInMainHand(item)

                                    refresh()
                                }
                            }
                        }
                }

                index++
            }

            slot(8, 2) {
                val (cost, level) = getEnchantMax(playerData, enchantLevel)
                val hasEnough = cost > 0 && cost <= playerData.gold

                item = item(Material.GOLD_BLOCK) {
                    name = "<yellow><u>Max".parse()
                    lore =
                        if (hasEnough) {
                            listOf(
                                "",
                                "<main>ɪɴꜰᴏʀᴍᴀᴛɪᴏɴ",
                                "<main><b>|</b> <gray>ᴄᴜʀʀᴇɴᴛ ʟᴇᴠᴇʟ: <white>${enchantLevel} <gray>→ <white>${level}",
                                "<main><b>|</b> <gray>ᴛᴏᴛᴀʟ: <white>${level - enchantLevel}",
                                "<main><b>|</b> <gray>ᴄᴏꜱᴛ: <yellow>⛁${cost.format()}",
                                "",
                                "<green><i>Click to upgrade"
                            ).map { it.parse() }
                        } else {
                            listOf(
                                "",
                                "<red>You don't have enough gold to upgrade this enchant!",
                                "<red>Come back when you have enough!",
                                ""
                            ).map { it.parse() }
                        }
                    onClick {
                        if (!hasEnough) return@onClick

                        playerData.gold -= cost

                        val item =
                            PickaxeEnchanter.enchantPickaxe(heldItem, enchant, (level - enchantLevel))
                        player.inventory.setItemInMainHand(item)

                        refresh()
                    }
                }
            }

            slot(9, 3) {
                item = item(Material.DARK_OAK_DOOR) {
                    name = "<red>Back".parse()
                    lore = listOf("", "<red>Click to go back.").map { it.parse() }
                    glowing = true
                    onClick { EnchantGUI(player).open() }
                }
            }
        }
    }

    fun open() {
        val gui = createGUI()
        player.openGUI(gui)
    }
}
