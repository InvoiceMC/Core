package me.outspending.core.commands.data

import me.outspending.core.commands.enums.SenderType

data class SubCommandData(
    val name: String,
    val description: String = "",
    val permission: String = "",
    val permissionMessage: String = DEFAULT_PERMISSION_MESSAGE,
    val senderType: SenderType = SenderType.BOTH,
    val cooldown: Long = 0
)
