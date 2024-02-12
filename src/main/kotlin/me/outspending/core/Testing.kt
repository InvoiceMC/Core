package me.outspending.core

import me.outspending.core.storage.serializers.ListSerializer

fun main() {
    val list = mutableListOf(1, 2, 3, 4, 5)
    println(ListSerializer<Int>().serialize(list))
}
