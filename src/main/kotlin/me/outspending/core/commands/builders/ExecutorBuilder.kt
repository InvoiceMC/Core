package me.outspending.core.commands.builders

import me.outspending.core.commands.CooldownManager
import me.outspending.core.commands.annotations.Catcher
import me.outspending.core.commands.data.CommandData
import me.outspending.core.commands.enums.SenderType
import me.outspending.core.instance
import me.outspending.core.utils.helpers.FormatHelper.Companion.parse
import org.bukkit.OfflinePlayer
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.valueParameters

class ExecutorBuilder(
    private val command: CommandData,
    private val cooldownManager: CooldownManager,
    private val mainFunction: KFunction<*>,
    private val subFunctions: List<KFunction<*>>
) {

    fun build() = CommandExecutor { sender, _, _, args ->

        val senderType = SenderType.fromSender(sender)

        // Make sure player has permission to run the main command scope
        if (!checkForPermission(sender, command.permission)) {
            sender.sendMessage(command.permissionMessage) // TODO: Include proper formatting
            return@CommandExecutor true
        }

        val commandInstance = command.clazz.constructors.first().call()

        val function: KFunction<*>
        val parsedArgs: Array<*>
        var cooldownId = command.name.lowercase()
        var cooldown = command.cooldown

        val params: List<KParameter>

        var requiredSender = command.senderType

        // Main `onCommand` function
        if (args.isEmpty() || command.subCommands.isEmpty()) {

            parsedArgs = parseCommandArgs(mainFunction, args)
            params = mainFunction.valueParameters.slice(1 until mainFunction.valueParameters.size)
            function = mainFunction
        }
        // SubCommand function
        else {

            val subCommandName = args[0].lowercase()
            val subArgs = args.copyOfRange(1, args.size)
            val subFunction = subFunctions.firstOrNull { it.name == subCommandName } ?: run {
                subCommandNotFound(sender, subCommandName)
                return@CommandExecutor true
            }
            val subCommand = command.subCommands[subCommandName] ?: run {
                subCommandNotFound(sender, subCommandName)
                return@CommandExecutor true
            }
            requiredSender = subCommand.senderType

            // Make sure player has permission to run the sub command scope
            if (!checkForPermission(sender, subCommand.permission)) {
                sender.sendMessage(subCommand.permissionMessage.parse(true))
                return@CommandExecutor true
            }

            cooldownId += ".$subCommandName"

            parsedArgs = parseCommandArgs(subFunction, subArgs)
            params = subFunction.valueParameters.slice(1 until subFunction.valueParameters.size)
            function = subFunction
            cooldown = subCommand.cooldown.takeIf { it > 0 } ?: command.cooldown
        }

        // Make sure the sender is proper type
        if (requiredSender != SenderType.BOTH && requiredSender != senderType) {
            sender.sendMessage("<gray>This command can only be used by <main>${requiredSender.name.lowercase()}<gray>s".parse(true))
            return@CommandExecutor true
        }

        val requiredParams = params.filterNot { it.type.isMarkedNullable }
//        val optionalParams = params.filter { it.type.isMarkedNullable }

        // Make sure all required parameters are present
        if (parsedArgs.filterNotNull().size < requiredParams.size) {
            sender.sendMessage("<gray>Invalid usage: <main>...".parse(true))
            return@CommandExecutor true
        }

        // Make sure the player is not on cooldown
        if (sender is Player && cooldown > 0) {
            if (cooldownManager.isOnCooldown(sender, cooldownId)) {
                val timeLeft = cooldownManager.timeLeft(sender, cooldownId)
                sender.sendMessage("<gray>You are on cooldown for <main>$timeLeft <gray>ms".parse(true))
                return@CommandExecutor true
            }
        }

        run {
            // Print what we are doing
            println("Calling ${function.name} with args: ${parsedArgs.joinToString(", ")}")
            println("Sender: $sender")
            println("Sender type: ${sender::class}")
        }

        // Execute command
        val result = function.call(commandInstance, sender, *parsedArgs)

        // Set cooldown
        if (sender is Player && cooldown > 0) {
            cooldownManager.setCooldown(sender, cooldownId, cooldown)
        }

        true
    }

    private fun parseCommandArgs(method: KFunction<*>, args: Array<String>): Array<*> {

        // Slice the sender
        val parameters = method.valueParameters.slice(1 until method.valueParameters.size)
        val parsedArgs = arrayOfNulls<Any>(parameters.size)

        for ((index, arg) in args.withIndex()) {

            val parameter = parameters.getOrNull(index) ?: break
            val type = parameter.type

            if (parameter.hasAnnotation<Catcher>()) {
                parsedArgs[index] = args.slice(index until args.size).joinToString(" ")
                break
            }

            parsedArgs[index] = when(type.classifier) {
                String::class -> arg
                Int::class -> arg.toIntOrNull()
                Double::class -> arg.toDoubleOrNull()
                Float::class -> arg.toFloatOrNull()
                Boolean::class -> arg.toBoolean()
                Player::class -> instance.server.getPlayer(arg)
                OfflinePlayer::class -> instance.server.getOfflinePlayer(arg)
                else -> null
            }
        }

        return parsedArgs
    }

    private val subCommandNotFound = { sender: CommandSender, name: String -> {
        sender.sendMessage("<gray>Unknown subcommand: <main>$name".parse(true))
    }}

    private fun checkForPermission(player: CommandSender, permission: String): Boolean {
        return if (permission.isEmpty()) true else player.hasPermission(permission)
    }
}