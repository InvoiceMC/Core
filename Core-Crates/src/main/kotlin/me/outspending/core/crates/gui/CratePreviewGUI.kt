package me.outspending.core.crates.gui

import me.tech.mcchestui.GUI

interface CratePreviewGUI {
    fun createGUI(): GUI

    fun open()
}
