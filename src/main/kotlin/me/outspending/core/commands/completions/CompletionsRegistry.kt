package me.outspending.core.commands.completions

import org.reflections.Reflections

object CompletionsRegistry {

    private val completions = mutableMapOf<String, Completion>()

    fun registerCompletions() {
        val completionsPackage = Reflections("me.outspending.core.commands.completions.impl")
        val completions = completionsPackage.getSubTypesOf(Completion::class.java)

        for (completion in completions) {
            val instance = completion.constructors.first().newInstance() as Completion
            val name = completion.simpleName.lowercase().replace("completion", "")
            this.completions[name] = instance
        }
    }

    fun getCompletion(name: String): Completion? {
        return completions[name]
    }
}