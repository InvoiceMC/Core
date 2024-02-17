package me.outspending.core.misc

import java.util.*
import kotlin.random.Random

val random: Random = Random.Default

class WeightedCollection<T> : Iterable<T> {
    private val items: NavigableMap<Double, T> = TreeMap()
    private var totalWeight: Double = 0.0

    fun add(weight: Double, item: T): WeightedCollection<T> {
        if (weight <= 0) return this

        totalWeight += weight
        items[totalWeight] = item
        return this
    }

    fun remove(item: T): WeightedCollection<T> {
        val iterator = items.entries.iterator()
        while (iterator.hasNext()) {
            val entry = iterator.next()
            if (entry.value == item) {
                iterator.remove()
                totalWeight -= entry.key
            }
        }

        return this
    }

    fun next(): T {
        val value: Double = random.nextDouble() * totalWeight
        return items.higherEntry(value).value
    }

    fun nextAndRemove(): T {
        val nextItem = next()
        remove(nextItem)

        return nextItem
    }

    override fun iterator(): Iterator<T> = items.values.iterator()
}
