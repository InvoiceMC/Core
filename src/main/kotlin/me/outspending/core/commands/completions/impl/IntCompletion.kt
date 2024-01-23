package me.outspending.core.commands.completions.impl

import me.outspending.core.commands.completions.Completion

class IntCompletion: Completion {

    override fun complete(): List<String> {
        return (0..100).map { it.toString() }.toList()
    }
}