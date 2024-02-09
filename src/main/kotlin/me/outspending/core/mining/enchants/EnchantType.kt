package me.outspending.core.mining.enchants

import me.outspending.core.mining.enchants.types.GoldFinderEnchant
import me.outspending.core.mining.enchants.types.MerchantEnchant

enum class EnchantType(val enchant: PickaxeEnchant) {
    MERCHANT(MerchantEnchant()),
    GOLDFINDER(GoldFinderEnchant()),
}