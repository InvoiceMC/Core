package me.outspending.core.mining.enchants.gui

import me.tech.mcchestui.GUI

interface EnchantGUI {
    fun createGUI(): GUI

    fun open()
}
