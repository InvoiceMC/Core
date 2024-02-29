package me.outspending.core.registry

fun interface Registrable {
    fun register(vararg packages: String)
}