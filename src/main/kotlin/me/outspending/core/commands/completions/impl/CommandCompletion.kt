package me.outspending.core.commands.completions.impl

import me.outspending.core.commands.CommandRegistry
import me.outspending.core.commands.completions.Completion

class CommandCompletion: Completion {

    override fun complete(): List<String> {
        return CommandRegistry.commands.map { it.name }
    }
}