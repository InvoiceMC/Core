package me.outspending.core.enchants.gui

import me.outspending.core.enchants.PickaxeEnchant
import kotlin.math.pow

object GUIUtils {
    private val enchantPow: Double = 1.005

    internal fun calculateEnchantCost(
        enchant: PickaxeEnchant,
        enchantLevel: Int,
        amount: Int
    ): Double {
        return enchant.getInitialCost() *
                ((enchantPow.pow(enchantLevel.toDouble()) - 1) / (enchantPow - 1)) *
                amount
    }
}