package me.outspending.core.commands

import me.outspending.core.commands.annotations.Command
import me.outspending.core.commands.builders.CommandBuilder
import me.outspending.core.instance
import me.outspending.core.utils.helpers.TimerHelper
import org.bukkit.Bukkit
import org.bukkit.command.PluginCommand
import org.reflections.Reflections
import org.reflections.scanners.SubTypesScanner
import org.reflections.scanners.TypeAnnotationsScanner
import org.reflections.util.ClasspathHelper
import org.reflections.util.ConfigurationBuilder
import kotlin.reflect.KClass

class CommandRegistry {
    private val logger = instance.logger
    private val commandMap = Bukkit.getCommandMap()
    private val builderConstructor = PluginCommand::class.java.declaredConstructors.first()

    init {
        builderConstructor.isAccessible = true
    }

    fun registerCommands() {

        val start = TimerHelper()

        val pkg = Reflections(
            ConfigurationBuilder()
            .setUrls(ClasspathHelper.forPackage("me.outspending.core.commands.impl"))
            .setScanners(SubTypesScanner(), TypeAnnotationsScanner()))
        val commands = pkg.getTypesAnnotatedWith(Command::class.java)

        for (command in commands) {
            val builder = registerCommand(command.kotlin)
            commandMap.register(builder.label, builder)
        }

        val elapsed = start.formattedElapsed()

        logger.info("Registered ${commands.size} commands in $elapsed")
    }

    private fun registerCommand(clazz: KClass<*>): PluginCommand {

        logger.info("Registering command ${clazz.simpleName}")

        val parsedCommand = CommandBuilder(clazz)
        val info = parsedCommand.commandInfo

        val builder = builderConstructor.newInstance(info.name, instance) as PluginCommand
        builder.apply {
            aliases = info.aliases.toList()
            description = info.description
            label = name
            setExecutor(parsedCommand.executor)
            tabCompleter = parsedCommand.tabCompleter
        }

        return builder
    }
}
