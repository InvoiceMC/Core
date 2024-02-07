package me.outspending.core.utils

import net.md_5.bungee.api.ChatColor

object ColorUtils {
    private val HEX_REGEX: Regex = Regex("&(#[a-fA-F0-9]{6})")

    @JvmStatic
    fun colorizeHex(string: String): String {
        return ChatColor.translateAlternateColorCodes(
            '&',
            HEX_REGEX.replace(string) { matchResult ->
                ChatColor.of(matchResult.groupValues[1]).toString()
            },
        )
    }
}
