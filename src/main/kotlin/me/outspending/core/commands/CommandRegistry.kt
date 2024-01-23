package me.outspending.core.commands

import me.outspending.core.commands.annotations.Command
import me.outspending.core.commands.builders.CommandBuilder
import me.outspending.core.commands.data.CommandData
import me.outspending.core.instance
import org.bukkit.Bukkit
import org.bukkit.command.PluginCommand
import org.reflections.Reflections
import org.reflections.scanners.SubTypesScanner
import org.reflections.scanners.TypeAnnotationsScanner
import org.reflections.util.ClasspathHelper
import org.reflections.util.ConfigurationBuilder
import kotlin.reflect.KClass
import kotlin.time.measureTime

object CommandRegistry {
    private val logger = instance.logger
    private val commandMap = Bukkit.getCommandMap()
    private val builderConstructor = PluginCommand::class.java.declaredConstructors.first()
    val commands: MutableList<CommandData> = mutableListOf()

    init {
        builderConstructor.isAccessible = true
    }

    fun registerCommands() {

        val pkg = Reflections(
            ConfigurationBuilder()
                .setUrls(ClasspathHelper.forPackage("me.outspending.core.commands.impl"))
                .setScanners(SubTypesScanner(), TypeAnnotationsScanner()))
        val commands = pkg.getTypesAnnotatedWith(Command::class.java)
        val time = measureTime {
            for (command in commands) {
                val builder = registerCommand(command.kotlin)
                commandMap.register(builder.label, builder)
            }
        }

        logger.info("Registered commands in $time")
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

        commands.add(info)

        return builder
    }
}
