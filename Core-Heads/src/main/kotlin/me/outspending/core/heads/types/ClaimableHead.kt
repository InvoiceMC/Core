package me.outspending.core.heads.types

import com.mojang.authlib.GameProfile
import com.mojang.authlib.properties.Property
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.entity.SkullBlockEntity
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.craftbukkit.v1_20_R3.CraftWorld
import org.spongepowered.configurate.yaml.internal.snakeyaml.external.biz.base64Coder.Base64Coder
import java.util.*


class ClaimableHead(private val loc: Location, private val url: String, private val rotation: Int) : IHead {

    private fun getRandomString(): String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return (1..10).map { allowedChars.random() }.joinToString("")
    }

    init {
        val world = loc.world
        val block = world.getBlockAt(loc)
        block.blockData = Bukkit.createBlockData("minecraft:player_head[rotation=$rotation]")
        val profile = GameProfile(UUID.randomUUID(), getRandomString())
        val encodedData = Base64Coder.encodeString(String.format("{textures:{SKIN:{url:\"%s\"}}}",
            "http://textures.minecraft.net/texture/$url"
        )).toString()
        profile.properties.put("textures", Property("textures", encodedData))
        val skullTile: SkullBlockEntity =
            (world as CraftWorld).handle.getBlockEntity(
                BlockPos(
                    loc.blockX,
                    loc.blockY,
                    loc.blockZ
                )
            ) as SkullBlockEntity
        skullTile.setOwner(profile)
        block.state.update(true)
    }

    override fun getLocation(): Location = loc

}