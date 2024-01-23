package me.outspending.core.enchants

import me.outspending.core.enchants.types.MerchantEnchant
import me.outspending.core.instance
import me.outspending.core.storage.PlayerData
import me.outspending.core.utils.PersistentUtils
import me.outspending.core.utils.Utilities.fix
import me.outspending.core.utils.Utilities.format
import me.outspending.core.utils.Utilities.getData
import me.outspending.core.utils.Utilities.toComponent
import me.outspending.core.utils.helpers.FormatHelper.Companion.parse
import me.tech.mcchestui.GUI
import me.tech.mcchestui.GUIType
import me.tech.mcchestui.item.item
import me.tech.mcchestui.utils.gui
import me.tech.mcchestui.utils.openGUI
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.persistence.PersistentDataType
import kotlin.math.pow

// TODO: 1. Split this into multiple files and clean it up
// TODO: 2. Add a way to view all enchants on the player's pickaxe
// TODO: 3. Add a back button to the enchant upgrade GUI
// TODO: 4. Add a max upgrade button (Maybe)

object EnchantGUI {
    private val enchantAmounts: Array<Int> = arrayOf(1, 25, 50, 100, 500, 1000)
    private val enchantPow: Double = 1.05

    fun openGUI(player: Player) {
        val dataContainer: PersistentDataContainer =
            PersistentUtils.getPersistentData(player.inventory.itemInMainHand)

        player.getData()?.let { data ->
            val gui: GUI =
                gui(
                    plugin = instance,
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
                                            "<gray>You currently have: <#e3af7b>${data.gold.format()} gold",
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

                    createEnchantButton(player, data, this, dataContainer, MerchantEnchant())
                }

            player.openGUI(gui)
        }
    }

    private fun upgradeEnchantGUI(
        player: Player,
        persistentDataContainer: PersistentDataContainer,
        enchant: PickaxeEnchant
    ) {
        player.getData()?.let { playerData ->
            val upgradeGUI: GUI =
                gui(
                    plugin = instance,
                    title = "Upgrade ${enchant.getEnchantName()} Enchant".toComponent(),
                    type = GUIType.Chest(rows = 3),
                ) {
                    fill(1, 1, 9, 4) { item = item(Material.GRAY_STAINED_GLASS_PANE) }

                    val newGUI = this
                    val enchantLevel = enchant.getEnchantmentLevel(persistentDataContainer)
                    for ((lineNum, i) in enchantAmounts.withIndex()) {
                        val enchantValue =
                            enchant.getInitialCost() *
                                ((enchantPow.pow(enchantLevel.toDouble()) - 1) / (enchantPow - 1)) *
                                i

                        slot((lineNum + 2), 2) {
                            if (playerData.gold >= enchantValue) {
                                item =
                                    item(Material.LIME_STAINED_GLASS_PANE) {
                                        name = "<dark_green><u>+$i</u>".toComponent()
                                        lore =
                                            listOf(
                                                    "",
                                                    "<gray>ᴄᴜʀʀᴇɴᴛ ʟᴇᴠᴇʟ: <white>$enchantLevel",
                                                    "<gray>ᴄᴏꜱᴛ: <yellow>⛁${enchantValue.format()} <gold>ɢᴏʟᴅ",
                                                    "",
                                                    "<green><u><i>Click to upgrade"
                                                )
                                                .map { it.parse() }
                                        onClick {
                                            upgradeEnchant(
                                                player,
                                                persistentDataContainer,
                                                enchant,
                                                i
                                            )
                                            newGUI.refresh()
                                        }
                                    }

                                return@slot
                            }
                            item =
                                item(Material.RED_STAINED_GLASS_PANE) {
                                    name = "<dark_red><u>+$i</u>".toComponent()
                                    lore =
                                        listOf(
                                                "",
                                                "<gray>ᴄᴜʀʀᴇɴᴛ ʟᴇᴠᴇʟ: <white>$enchantLevel",
                                                "<gray>ᴄᴏꜱᴛ: <yellow>⛁${enchantValue.format()} <gold>ɢᴏʟᴅ",
                                                "",
                                                "<red><u>You need more gold to upgrade this enchant!"
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

    // TODO: Make sure this adds the NBT to the pickaxe and updates the pickaxe lore
    private fun upgradeEnchant(
        player: Player,
        persistentDataContainer: PersistentDataContainer,
        enchant: PickaxeEnchant,
        amount: Int
    ) {
        Bukkit.broadcast("<rainbow>Wow $amount".parse())
    }

    private fun createEnchantButton(
        player: Player,
        playerData: PlayerData,
        gui: GUI,
        persistentDataContainer: PersistentDataContainer,
        enchant: PickaxeEnchant,
        vararg description: String
    ) {
        val enchantLevel: Int =
            persistentDataContainer.getOrDefault(
                NamespacedKey("enchant", enchant.getEnchantName()),
                PersistentDataType.INTEGER,
                0
            )

        val totalCost = enchant.getInitialCost() * 1.2.pow(enchantLevel.toDouble())
        val enchantName =
            enchant.getEnchantName().replaceFirstChar {
                if (it.isLowerCase()) it.titlecase() else it.toString()
            }

        return gui.slot(2, 4) {
            item =
                item(if (playerData.gold >= totalCost) Material.LIME_DYE else Material.RED_DYE) {
                    name = "<#e3af7b>${enchantName} Enchant".toComponent()
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
                    onClick { upgradeEnchantGUI(player, persistentDataContainer, enchant) }
                }
        }
    }
}
