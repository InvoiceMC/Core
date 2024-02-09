package me.outspending.core.mining.enchants

data class EnchantResult(var money: Double = 0.0, var gold: Int = 1, var xp: Int = 0) {
    operator fun plusAssign(enchantResult: EnchantResult) {
        money += enchantResult.money
        gold += enchantResult.gold
        xp += enchantResult.xp
    }

    fun add(enchantResult: EnchantResult) {
        money += enchantResult.money
        gold += enchantResult.gold
        xp += enchantResult.xp
    }

    fun add(money: Double, gold: Int, xp: Int) {
        this.money += money
        this.gold += gold
        this.xp += xp
    }

    fun isEmpty(): Boolean = money == 0.0 && gold == 0 && xp == 0
}
