package me.outspending.core.commands.annotations

@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class Tab(
    val list: String
)
