package me.outspending.core.enchants

import me.outspending.core.enchants.types.GoldFinderEnchant
import me.outspending.core.enchants.types.MerchantEnchant

enum class EnchantType(val enchant: PickaxeEnchant) {
    MERCHANT(MerchantEnchant()),
    GOLDFINDER(GoldFinderEnchant()),
}