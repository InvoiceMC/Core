package me.outspending.core.utils

import java.util.*
import kotlin.random.Random

class WeightedCollection<T> : Iterable<T> {
    val random: Random = Random.Default

    private val items: NavigableSet<WeightedItem<T>> = TreeSet()
    private var total: Double = 0.0

    fun add(
        weight: Double,
        value: T,
    ) {
        if (weight <= 0) return

        total += weight
        items.add(WeightedItem(total, value))
    }

    fun remove(value: T): Boolean {
        val iterator = items.iterator()

        while (iterator.hasNext()) {
            val item = iterator.next()

            if (item.value == value) {
                iterator.remove()
                total -= item.weight
                return true
            }
        }

        return false
    }

    fun values(): List<T> {
        return items.map { it.value }
    }

    override fun iterator(): Iterator<T> {
        return values().iterator()
    }

    private data class WeightedItem<T>(val weight: Double, val value: T)
}
