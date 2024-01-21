package me.outspending.core.enchants

data class EnchantResult(var money: Double, var gold: Int, var xp: Int) {
    companion object {
        val EMPTY = EnchantResult(0.0, 0, 0)
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