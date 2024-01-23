package me.outspending.core.commands.annotations

@Target(AnnotationTarget.CLASS)
annotation class Command(
    val name: String,
    val description: String = "No description",
    val aliases: Array<String> = [],
    val permission: String = "",
    val permissionMessage: String = "You do not have permission to use this command",
    val cooldown: Long = 0, // In milliseconds
)
