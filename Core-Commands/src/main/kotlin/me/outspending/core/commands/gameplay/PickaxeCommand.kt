package me.outspending.core.commands.gameplay

import com.azuyamat.maestro.common.annotations.Command
import me.outspending.core.config.messagesConfig
import me.outspending.core.helpers.FormatHelper.Companion.parse
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack

@Command(
    name = "pickaxe",
)
class PickaxeCommand {

    fun onCommand(player: Player) {
        val itemStack = ItemStack(Material.DIAMOND_PICKAXE)
        itemStack.editMeta() { meta ->
            meta.displayName(
                "<main>${player.name}'s</main> <gray>Pickaxe"
                    .parse()
            )
            meta.lore(
                listOf(
                    "<dark_gray>ᴘɪᴄᴋᴀxᴇ",
                    "",
                    "<main><b>ɪɴꜰᴏʀᴍᴀᴛɪᴏɴ</b></main>",
                    " <second><b>|</b> <gray>ᴏᴡɴᴇʀ: <white>${player.name}",
                    " <second><b>|</b> <gray>ʙʟᴏᴄᴋꜱ ʙʀᴏᴋᴇɴ: <white>0",
                    "",
                    "<main><b>ᴇɴᴄʜᴀɴᴛꜱ</b></main>",
                    "",
                )
                    .map { it.parse() }
            )

            meta.addEnchant(Enchantment.DIG_SPEED, 1000, true)
            meta.isUnbreakable = true

            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ATTRIBUTES)
        }

        player.inventory.addItem(itemStack)
        player.sendMessage(messagesConfig.getMessage("commands.gameplay.pickaxe_success"))
    }
}
