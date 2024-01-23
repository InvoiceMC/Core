package me.outspending.core.commands.data

import me.outspending.core.commands.enums.SenderType
import kotlin.reflect.KClass

const val DEFAULT_PERMISSION_MESSAGE = "<gray>You do not have permission to use this command<reset>"

data class CommandData(
    val clazz: KClass<*>,
    val name: String,
    val description: String = "",
    val aliases: Array<String> = arrayOf(),
    val permission: String = "",
    val permissionMessage: String = DEFAULT_PERMISSION_MESSAGE,
    val senderType: SenderType = SenderType.BOTH,
    val cooldown: Long = 0,
    val subCommands: Map<String, SubCommandData> = mutableMapOf(),
)
