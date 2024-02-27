package me.outspending.core.creator

interface Creator<T> {
    fun build(): T
}