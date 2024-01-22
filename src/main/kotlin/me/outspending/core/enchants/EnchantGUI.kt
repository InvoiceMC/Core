package me.outspending.core.enchants

import me.outspending.core.Core
import me.outspending.core.enchants.types.GoldFinderEnchant
import me.outspending.core.storage.PlayerData
import me.outspending.core.utils.PersistentUtils
import me.outspending.core.utils.Utilities.Companion.fix
import me.outspending.core.utils.Utilities.Companion.format
import me.outspending.core.utils.Utilities.Companion.getData
import me.outspending.core.utils.Utilities.Companion.toComponent
import me.sparky983.vision.Button
import me.sparky983.vision.Gui
import me.sparky983.vision.ItemType
import me.sparky983.vision.Slot
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.persistence.PersistentDataType
import kotlin.math.pow

// val totalCost =
//            enchant.getInitialCost() *
//                ((pow(1.2, enchantLevel.toDouble()) - 1) / (1.2 - 1)) *
//                enchantAmount

object EnchantGUI {

    fun openGUI(player: Player) {
        val dataContainer: PersistentDataContainer =
            PersistentUtils.getPersistentData(player.inventory.itemInMainHand)

        player.getData()?.let { data ->
            val gui: Gui =
                Gui.chest()
                    .rows(6)
                    // Fill background
                    .fill(Button.of(ItemType.GRAY_STAINED_GLASS_PANE))

                    // Prestige Enchant Button
                    .button(
                        Slot.of(1, 2),
                        Button.of(ItemType.PURPLE_DYE)
                            .name("<#c97be3>ᴘʀᴇꜱᴛɪɢᴇ ᴇɴᴄʜᴀɴᴛꜱ".toComponent())
                            .lore(
                                listOf(
                                        "",
                                        "<gray>Here you will find the prestige enchants, these enchants are",
                                        "<gray>obtainable by prestiging and are very powerful.",
                                        "",
                                        "<#c97be3><i><u>ᴄʟɪᴄᴋ ᴛᴏ ᴠɪᴇᴡ"
                                    )
                                    .map { it.toComponent() }
                            )
                    )

                    // Coin Merchant Button
                    .button(
                        Slot.of(1, 4),
                        Button.of(ItemType.SUNFLOWER)
                            .name("<#e3af7b>ɢᴏʟᴅ ᴍᴇʀᴄʜᴀɴᴛ".toComponent())
                            .lore(
                                listOf(
                                        "",
                                        "<gray>Here at the gold merchant you can trade money for gold!",
                                        "",
                                        "<gray>You currently have: <#e3af7b>${data.gold.format()} gold",
                                        "",
                                        "<#e3af7b><i><u>ᴄʟɪᴄᴋ ᴛᴏ ᴠɪᴇᴡ"
                                    )
                                    .map { it.toComponent() }
                            )
                    )

                    // Coming Soon
                    .button(
                        Slot.of(1, 6),
                        Button.of(ItemType.BARRIER)
                            .name("<#ff3333>ᴄᴏᴍɪɴɢ ꜱᴏᴏɴ".toComponent())
                            .lore(
                                listOf(
                                        "",
                                        "<gray>This feature is coming soon!",
                                        "",
                                    )
                                    .map { it.toComponent() }
                            )
                    )

                    // Enchant Buttons
                    .button(
                        Slot.of(3, 1),
                        createEnchantButton(
                            player,
                            data,
                            dataContainer,
                            GoldFinderEnchant(),
                            "<gray><i>Chance to find gold whilst mining!"
                        )
                    )
                    .title("Enchantments".toComponent())
                    .build()

            Core.paperVision.open(player, gui)
        }
    }

    private fun upgradeEnchantGUI(
        player: Player,
        persistentDataContainer: PersistentDataContainer,
        enchant: PickaxeEnchant
    ) {
        val gui: Gui = Gui.chest().rows(3).fill(Button.of(ItemType.GRAY_STAINED_GLASS_PANE)).build()

        Core.paperVision.open(player, gui)
    }

    private fun upgradeEnchant(player: Player) {}

    private fun updateLore(item: ItemStack, lore: List<String>): ItemStack {
        item.editMeta { meta -> meta.lore(lore.map { it.toComponent() }) }

        return item
    }

    private fun createEnchantButton(
        player: Player,
        playerData: PlayerData,
        persistentDataContainer: PersistentDataContainer,
        enchant: PickaxeEnchant,
        vararg description: String
    ): Button {
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

        return Button.of(if (playerData.gold >= totalCost) ItemType.LIME_DYE else ItemType.RED_DYE)
            .name("<#e3af7b>${enchantName} Enchant".toComponent())
            .lore(
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
            )
            .onClick { upgradeEnchantGUI(player, persistentDataContainer, enchant) }
    }
}
