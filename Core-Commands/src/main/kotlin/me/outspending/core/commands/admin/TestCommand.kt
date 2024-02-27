package me.outspending.core.commands.admin

import com.azuyamat.maestro.common.annotations.Command
import me.outspending.core.creator.ItemCreator
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

@Command(name = "test", permission = "core.test", description = "wyd here nerd")
class TestCommand {

    fun onCommand(player: Player) {
        val item: ItemStack = ItemCreator(Material.PAPER)
            .name("<rainbow>Test Item!")
            .type("contract")
            .lore("Woah, this was created with", "the ItemCreator class!", "", "Last Line, Here!")
            .build()

        player.inventory.addItem(item)
    }
}
