package me.outspending.core.enchants.gui

import me.outspending.core.Core
import me.outspending.core.enchants.EnchantType
import me.outspending.core.enchants.gui.GUIUtils.calculateEnchantCost
import me.outspending.core.storage.data.PlayerData
import me.outspending.core.utils.PersistentUtils
import me.outspending.core.utils.Utilities.fix
import me.outspending.core.utils.Utilities.format
import me.outspending.core.utils.Utilities.getData
import me.outspending.core.utils.Utilities.toComponent
import me.outspending.core.utils.Utilities.toUpperCase
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

// TODO: 1. Add a way to view all enchants on the player's pickaxe
// TODO: 2. Add a max upgrade button (Maybe)

class EnchantGUI(private val player: Player) {
    fun openGUI() {
        val dataContainer: PersistentDataContainer =
            PersistentUtils.getPersistentData(player.inventory.itemInMainHand)

        player.getData()?.let { data ->
            val gui: GUI =
                gui(
                    plugin = Core.instance,
                    title = "Enchantments".toComponent(),
                    type = GUIType.Chest(rows = 6),
                ) {
                    fill(1, 1, 9, 6) { item = item(Material.GRAY_STAINED_GLASS_PANE) }

                    // Prestige Enchants
                    slot(3, 2) {
                        item =
                            item(Material.PURPLE_DYE) {
                                name = "<#c97be3>ᴘʀᴇꜱᴛɪɢᴇ ᴇɴᴄʜᴀɴᴛꜱ".toComponent()
                                lore =
                                    listOf(
                                            "",
                                            "<gray>Here you will find the prestige enchants, these enchants are",
                                            "<gray>obtainable by prestiging and are very powerful.",
                                            "",
                                            "<#c97be3><i><u>ᴄʟɪᴄᴋ ᴛᴏ ᴠɪᴇᴡ"
                                        )
                                        .map { it.toComponent() }
                            }
                    }

                    // Gold Merchant
                    slot(5, 2) {
                        item =
                            item(Material.SUNFLOWER) {
                                name = "<#e3af7b>ɢᴏʟᴅ ᴍᴇʀᴄʜᴀɴᴛ".toComponent()
                                lore =
                                    listOf(
                                            "",
                                            "<gray>Here at the gold merchant you can trade money for gold!",
                                            "",
                                            "<#e3af7b><i><u>ᴄʟɪᴄᴋ ᴛᴏ ᴠɪᴇᴡ"
                                        )
                                        .map { it.toComponent() }
                            }
                    }

                    // Coming Soon
                    slot(7, 2) {
                        item =
                            item(Material.BARRIER) {
                                name = "<#ff3333>ᴄᴏᴍɪɴɢ ꜱᴏᴏɴ".toComponent()
                                lore =
                                    listOf(
                                            "",
                                            "<gray>This feature is coming soon!",
                                            "",
                                        )
                                        .map { it.toComponent() }
                            }
                    }

                    createEnchantButton(data, this, dataContainer, EnchantType.MERCHANT, 2, 4)
                    createEnchantButton(data, this, dataContainer, EnchantType.GOLDFINDER, 3, 4)
                }

            player.openGUI(gui)
        }
    }

    private fun upgradeEnchantGUI(enchantType: EnchantType) =
        UpgradeGUI(enchantType).showGUI(player)

    private fun createEnchantButton(
        playerData: PlayerData,
        gui: GUI,
        persistentDataContainer: PersistentDataContainer,
        enchantType: EnchantType,
        x: Int,
        y: Int,
        vararg description: String
    ) {
        val enchant = enchantType.enchant
        val enchantLevel: Int =
            persistentDataContainer.getOrDefault(
                NamespacedKey("enchant", enchant.getEnchantName()),
                PersistentDataType.INTEGER,
                0
            )

        val totalCost = calculateEnchantCost(enchant, enchantLevel, 1)
        return gui.slot(x, y) {
            item =
                item(if (playerData.gold >= totalCost) Material.LIME_DYE else Material.RED_DYE) {
                    name = "<#e3af7b>${enchant.getEnchantName().toUpperCase()} Enchant".toComponent()
                    lore =
                        listOf(
                                *description,
                                "",
                                "<gray>ᴄᴜʀʀᴇɴᴛ ʟᴇᴠᴇʟ: <#e3af7b>$enchantLevel",
                                "<gray>ᴄᴏꜱᴛ: <#e3af7b>$${totalCost.format()}",
                                "",
                                "<gray>ᴄʜᴀɴᴄᴇ: <#e3af7b>${enchant.getEnchantmentChance(enchantLevel).fix()}%",
                                "",
                                "<#7de37b><i><u>ᴄʟɪᴄᴋ ᴛᴏ ᴜᴘɢʀᴀᴅᴇ"
                            )
                            .map { it.toComponent() }
                    onClick { upgradeEnchantGUI(enchantType) }
                }
        }
    }
}
