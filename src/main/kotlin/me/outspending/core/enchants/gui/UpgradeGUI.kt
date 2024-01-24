package me.outspending.core.enchants.gui

import me.outspending.core.enchants.EnchantType
import me.outspending.core.enchants.PickaxeEnchant
import me.outspending.core.enchants.gui.GUIUtils.calculateEnchantCost
import me.outspending.core.instance
import me.outspending.core.utils.PersistentUtils
import me.outspending.core.utils.Utilities.format
import me.outspending.core.utils.Utilities.getData
import me.outspending.core.utils.Utilities.toComponent
import me.outspending.core.utils.Utilities.toUpperCase
import me.outspending.core.utils.helpers.FormatHelper.Companion.parse
import me.tech.mcchestui.GUI
import me.tech.mcchestui.GUIType
import me.tech.mcchestui.item.item
import me.tech.mcchestui.utils.gui
import me.tech.mcchestui.utils.openGUI
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.persistence.PersistentDataType

class UpgradeGUI(private val enchantType: EnchantType) {
    private val enchantAmounts: Array<Int> = arrayOf(1, 25, 50, 100, 500, 1000)

    fun showGUI(player: Player) {
        val dataContainer = PersistentUtils.getPersistentData(player.inventory.itemInMainHand)
        val enchant = enchantType.enchant
        player.getData()?.let { data ->
            val upgradeGUI: GUI =
                gui(
                    plugin = instance,
                    title = "Upgrade ${enchant.getEnchantName().toUpperCase()}".toComponent(),
                    type = GUIType.Chest(rows = 3),
                ) {
                    fill(1, 1, 9, 4) { item = item(Material.GRAY_STAINED_GLASS_PANE) }

                    slot(9, 3) {
                        item =
                            item(Material.DARK_OAK_DOOR) {
                                glowing = true

                                name = "<#c97be3>ɢᴏ ʙᴀᴄᴋ".toComponent()
                                lore =
                                    listOf("", "<gray>Click to go back the enchantment menu.", "")
                                        .map { it.toComponent() }
                                onClick { EnchantGUI(player).openGUI() }
                            }
                    }

                    val newGUI = this
                    val enchantLevel = enchant.getEnchantmentLevel(dataContainer)
                    for ((lineNum, i) in enchantAmounts.withIndex()) {
                        val enchantValue = calculateEnchantCost(enchant, enchantLevel, i)

                        slot((lineNum + 2), 2) {
                            val itemLore =
                                listOf(
                                    "",
                                    "<gray>ᴄᴜʀʀᴇɴᴛ ʟᴇᴠᴇʟ: <white>$enchantLevel",
                                    "<gray>ᴄᴏꜱᴛ: <yellow>⛁${enchantValue.format()} <gold>ɢᴏʟᴅ",
                                    "",
                                )

                            if (data.gold >= enchantValue) {
                                item =
                                    item(Material.LIME_STAINED_GLASS_PANE) {
                                        name = "<dark_green><u>+$i</u>".toComponent()
                                        lore =
                                            itemLore
                                                .plus("<green><u>Click to upgrade this enchant!")
                                                .map { it.parse() }
                                        onClick {
                                            data.gold -= enchantValue.toInt()

                                            upgradeEnchant(player, dataContainer, enchant, i)
                                            newGUI.refresh()
                                        }
                                    }

                                return@slot
                            }
                            item =
                                item(Material.RED_STAINED_GLASS_PANE) {
                                    name = "<dark_red><u>+$i</u>".toComponent()
                                    lore =
                                        itemLore
                                            .plus(
                                                "<red><u>You do not have enough gold to upgrade this enchant!"
                                            )
                                            .map { it.parse() }
                                }

                            return@slot
                        }
                    }
                }

            player.openGUI(upgradeGUI)
        }
    }

    private fun upgradeEnchant(
        player: Player,
        persistentDataContainer: PersistentDataContainer,
        enchant: PickaxeEnchant,
        amount: Int
    ) {
        val enchantName = enchant.getEnchantName()

        // Set the enchant amount of the NBT
        val key = NamespacedKey("enchant", enchantName)
        val enchantAmount = persistentDataContainer.getOrDefault(key, PersistentDataType.INTEGER, 0)
        persistentDataContainer.set(key, PersistentDataType.INTEGER, enchantAmount + amount)

        // Get lore values
        val enchantLore =
            persistentDataContainer.get(
                NamespacedKey("lore", enchantName),
                PersistentDataType.INTEGER
            )
        val playerItem = player.inventory.itemInMainHand

        // Edit Meta
        playerItem.editMeta { meta ->
            val itemLore = meta.lore()

            // Edit Meta Lore
            itemLore?.let { lore ->
                val string =
                    "<main><b>|</b> <gray>${enchantName.toUpperCase()}: <white>${enchantAmount + amount}".parse(false)
                // Check if the lore has the enchant already in it if not it will create it.
                if (enchantLore == null) {
                    val enchantLine = lore.size - 1 // Since there will be a space at the last line
                    lore.add(enchantLine, string)
                    persistentDataContainer.set(
                        NamespacedKey("lore", enchantName),
                        PersistentDataType.INTEGER,
                        enchantLine
                    )
                } else {
                    lore[enchantLore] = string
                }
            }

            // sets the lore of the metadata
            meta.lore(itemLore)
        }

        // Set the item in the player's hand
        player.inventory.setItemInMainHand(playerItem)
    }
}
