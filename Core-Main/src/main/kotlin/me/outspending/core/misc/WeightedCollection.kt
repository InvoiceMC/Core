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

    fun clear() {
        items.clear()
        totalWeight = 0.0
    }

    fun remove(item: T) {
        val entry = items.entries.find { it.value == item } ?: return
        items.remove(entry.key)
        totalWeight -= entry.key
    }

    fun normalize() {
        var currentWeight = 0.0
        val newItems = TreeMap<Double, T>()
        items.forEach { (weight, item) ->
            currentWeight += weight
            newItems[currentWeight] = item
        }
        items.clear()
        items.putAll(newItems)
        totalWeight = currentWeight
    }

    fun next(): T {
        val value: Double = random.nextDouble() * totalWeight
        return items.higherEntry(value).value
    }

    fun nextAndRemove(): T {
        val next = next()
        remove(next)

        return next
    }

    override fun iterator(): Iterator<T> = items.values.iterator()
}
