package me.outspending.core.mining.enchants

data class EnchantResult(
    var money: Double = 0.0,
    var gold: Int = 1,
    var xp: Int = 0,
    var blocks: Int = 0
) {
    operator fun plusAssign(enchantResult: EnchantResult) {
        money += enchantResult.money
        gold += enchantResult.gold
        xp += enchantResult.xp
        blocks += enchantResult.blocks
    }

    fun isEmpty(): Boolean = money == 0.0 && gold == 0 && xp == 0 && blocks == 0
}
