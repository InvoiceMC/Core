package me.outspending.core.crates.gui

import me.outspending.core.CoreHandler.core
import me.outspending.core.crates.types.ICrate
import me.outspending.core.helpers.FormatHelper.Companion.parse
import me.tech.mcchestui.GUI
import me.tech.mcchestui.GUIType
import me.tech.mcchestui.item.item
import me.tech.mcchestui.utils.gui
import me.tech.mcchestui.utils.openGUI
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.entity.Player

class PreviewCrateGUI(private val player: Player, private val crate: ICrate, private var page: Int) : CratePreviewGUI {

    override fun createGUI(): GUI {
        return gui(
            plugin = core,
            title = "Previewing <main>${crate.getDisplayName()} <gray>#$page".parse(),
            type = GUIType.Chest(6)
        ) {
            fillBorder { item = item(Material.GRAY_STAINED_GLASS_PANE) { name = " ".parse() } }

            val list = crate.getRewards().toList()
            var index = 2
            val max_items = 7 * 4
            val start = max_items * (page - 1)
            val end = if (start + max_items < list.size) start + max_items else list.size
            println("Start: $start, Max: $max_items, End: $end, Size: ${list.size}")
            println("if: ${(start + max_items < list.size)}")
            if (list.size < start) {
                player.sendMessage("<light_red>How did you get here?".parse(true))
            }

            for (reward in list.subList(start, end)) {
                val chance = reward.getChance()
                val rewardItem = reward.getItem().clone()
                val itemLore: MutableList<Component> = rewardItem.lore() ?: mutableListOf()
                itemLore.add("".parse())
                itemLore.add("<main>Chance: <second>${chance * 100}%".parse())
                itemLore.add("".parse())
                rewardItem.lore(itemLore)
                nextAvailableSlot { item =
                    item(rewardItem)
                }
                index++
            }
            if (page > 1) {
                println("Adding previous page!")
                slot(1, 6) { // and why the fuck is this y 6?????
                    item = item(Material.ARROW) { name = "<main>Previous Page".parse() }
                    onClick() {
                        PreviewCrateGUI(player, crate, page - 1).open()

                    }
                }
            }
            if (end < list.size) {
                println("Adding next page!")
                slot(0, 7) { // why the fuck does this go backwards and is the y + 2?????
                    item = item(Material.ARROW) { name = "<main>Next Page".parse() }
                    onClick() {
                        PreviewCrateGUI(player, crate, page + 1).open()
                    }
                }
            }
        }
    }

    override fun open() {
        val gui = createGUI()
        player.openGUI(gui)
    }
}
