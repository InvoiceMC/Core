package me.outspending.core.enchants.gui

import me.outspending.core.enchants.PickaxeEnchant
import kotlin.math.pow

object GUIUtils {
    internal fun calculateEnchantCost(
        enchant: PickaxeEnchant,
        enchantLevel: Int,
        amount: Int
    ): Double {
        val enchantPow = enchant.getIncreaseProgression() / 100
        return enchant.getInitialCost() *
                ((enchantPow.pow(enchantLevel.toDouble()) - 1) / (enchantPow - 1)) *
                amount
    }
}