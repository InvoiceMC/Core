package me.outspending.core.commands.admin

import com.azuyamat.maestro.bukkit.annotations.Command
import me.outspending.core.mining.MineUtils
import me.outspending.core.mining.shapes.SphereShape
import org.bukkit.entity.Player

@Command(name = "enchanttest", permission = "core.enchanttest", description = "wyd here nerd")
class EnchantTestCommand {

    fun onCommand(player: Player, radius: Int) {
        MineUtils.setBlocks(player, player.location, SphereShape(radius))
    }
}
