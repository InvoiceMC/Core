package me.outspending.core.commands.gameplay

import com.azuyamat.maestro.bukkit.annotations.Command
import me.outspending.core.Utilities.toComponent
import me.outspending.core.core
import me.outspending.core.misc.helpers.FormatHelper.Companion.parse
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
                "<gradient:#e37622:#deb799>${player.name}'s</gradient> <gray>Pickaxe"
                    .toComponent()
            )
            meta.lore(
                listOf(
                    "<dark_gray>Owned by ${player.name}",
                    "",
                    "<main>ɪɴꜰᴏʀᴍᴀᴛɪᴏɴ</main>",
                    "<main><b>|</b> <white>N/A",
                    "<main><b>|</b> <white>N/A",
                    "<main><b>|</b> <white>N/A",
                    "",
                    "<main>ᴇɴᴄʜᴀɴᴛꜱ</main>",
                    "",
                )
                    .map { it.parse(false) }
            )

            meta.addEnchant(Enchantment.DIG_SPEED, 1000, true)
            meta.isUnbreakable = true

            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE)
        }

        player.inventory.addItem(itemStack)
        player.sendMessage(core.messageConfig.getMessage("commands.gameplay.pickaxe_success"))
    }
}
