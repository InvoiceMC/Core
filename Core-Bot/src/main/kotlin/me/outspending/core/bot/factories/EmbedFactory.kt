package me.outspending.core.bot.factories

import me.outspending.core.bot.BotManager.discordConfig
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.MessageEmbed
import java.awt.Color

class EmbedFactory(private val title: String) {
    private var description: String? = null
    private var footer: String? = null
    private var color = discordConfig.getValue("embed-color").asStringOr("#000000")
    private var thumbnail: String? = null
    private var image: String? = null
    private val fields = mutableListOf<MessageEmbed.Field>()

    fun setDescription(description: String) = apply { this.description = description }
    fun setFooter(footer: String) = apply { this.footer = footer }
    fun setColor(color: String) = apply { this.color = color }
    fun setThumbnail(thumbnail: String) = apply { this.thumbnail = thumbnail }
    fun setImage(image: String) = apply { this.image = image }
    fun addField(name: String, value: String, inline: Boolean) = apply { fields.add(MessageEmbed.Field(name, value, inline)) }

    fun build(): MessageEmbed {
        val embedBuilder = EmbedBuilder()
            .setTitle(title)
            .setDescription(description)
            .setFooter(footer, null)
            .setThumbnail(thumbnail)
            .setImage(image)
            .setColor(Color.decode(color))

        fields.forEach { embedBuilder.addField(it) }

        return embedBuilder.build()
    }
}