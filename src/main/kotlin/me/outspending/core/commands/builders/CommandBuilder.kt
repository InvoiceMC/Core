package me.outspending.core.commands.builders

import me.outspending.core.commands.CooldownManager
import me.outspending.core.commands.annotations.Command
import me.outspending.core.commands.annotations.SubCommand
import me.outspending.core.commands.data.CommandData
import me.outspending.core.commands.data.SubCommandData
import me.outspending.core.commands.enums.SenderType
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.entity.Player
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.functions
import kotlin.reflect.full.valueParameters

class CommandBuilder(clazz: KClass<*>) {

    val cooldownManager = CooldownManager()
    private val annotation = clazz.findAnnotation<Command>()!! // !! is safe because we check for it in CommandRegistry before calling this

    private val functions = clazz.functions
    private val mainFunction = functions.find { it.name == "onCommand" } ?: run {
        throw Exception("No main command function found in ${clazz.simpleName}")
    }
    private val subFunctions = functions.filter { it.name != "onCommand" }

    private val subCommands = subFunctions.mapNotNull {
        val subAnnotation = it.findAnnotation<SubCommand>() ?: return@mapNotNull null
        val name = subAnnotation.name

        val senderType = getSenderType(it.valueParameters.first())

        val info = SubCommandData(
            name = name,
            description = subAnnotation.description,
            permission = subAnnotation.permission,
            permissionMessage = subAnnotation.permissionMessage,
            senderType = senderType,
            cooldown = subAnnotation.cooldown
        )

        name to info
    }.toMap()

    var commandInfo = CommandData(
        clazz = clazz,
        name = annotation.name,
        description = annotation.description,
        aliases = annotation.aliases,
        permission = annotation.permission,
        permissionMessage = annotation.permissionMessage,
        senderType = getSenderType(mainFunction.valueParameters.first()),
        cooldown = annotation.cooldown,
        subCommands = subCommands
    )

    val executor = ExecutorBuilder(
        commandInfo,
        cooldownManager,
        mainFunction,
        subFunctions
    ).build()

    val tabCompleter = TabCompleteBuilder(
        commandInfo,
        mainFunction,
        subFunctions
    ).build()

    companion object {
        fun getSenderType(parameter: KParameter) = when(parameter.type.classifier) {
            Player::class -> SenderType.PLAYER
            ConsoleCommandSender::class -> SenderType.CONSOLE
            else -> SenderType.BOTH
        }
    }
}