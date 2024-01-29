package me.outspending.core.commands.impl.admin

import com.azuyamat.maestro.bukkit.annotations.Command
import me.outspending.core.utils.MineUtils
import org.bukkit.entity.Player

@Command(name = "enchanttest", permission = "core.enchanttest", description = "wyd here nerd")
class EnchantTestCommand {

    fun onCommand(player: Player, radius: Int) {
        MineUtils.setBlocksSphere(player, player.location, radius)
    }
}
