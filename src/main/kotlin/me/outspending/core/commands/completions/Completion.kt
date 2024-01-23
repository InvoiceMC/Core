package me.outspending.core.commands.completions

interface Completion {

    fun complete(): List<String> = emptyList()
}