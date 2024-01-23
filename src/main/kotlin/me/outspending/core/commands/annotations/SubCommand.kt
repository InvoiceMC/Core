package me.outspending.core.commands.annotations

@Target(AnnotationTarget.FUNCTION)
annotation class SubCommand(
    val name: String,
    val description: String = "",
    val permission: String = "",
    val permissionMessage: String = "You do not have permission to use this command",
    val cooldown: Long = 0,
)
