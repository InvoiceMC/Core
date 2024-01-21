package me.outspending.core.utils

import kotlin.math.log10
import kotlin.math.pow
import me.outspending.core.utils.Utilities.Companion.removeDecimal

object NumberUtils {
    private val FORMAT_SUFFIXES: Array<String> =
        arrayOf(
            " ",
            "K",
            "M",
            "B",
            "T",
            "Q",
            "Qu",
            "S",
            "Sp",
            "O",
            "N",
            "D",
            "U",
            "Dc",
            "V",
            "Tg",
            "Pg",
            "Eg",
            "Zg",
            "Yg"
        )
    private val TINY_NUMBERS: Map<Char, Char> =
        mapOf(
            '0' to '⁰',
            '1' to '¹',
            '2' to '²',
            '3' to '³',
            '4' to '⁴',
            '5' to '⁵',
            '6' to '⁶',
            '7' to '⁷',
            '8' to '⁸',
            '9' to '⁹',
        )

    @JvmStatic
    fun format(double: Double): String {
        if (double < 1000) return "%.1f".format(double).removeDecimal()

        val exp = (log10(double) / log10(1000.0)).toInt()
        return "%.1f%s".format(double / 1000.0.pow(exp), FORMAT_SUFFIXES[exp]).removeDecimal()
    }

    @JvmStatic fun regex(double: Double): String = "%,.1f".format(double).removeDecimal()

    @JvmStatic fun fix(double: Double): String = "%.1f".format(double).removeDecimal()

    @JvmStatic
    fun tinyNumbers(integer: Int): String =
        integer.toString().map { TINY_NUMBERS[it] ?: it }.joinToString("")
}
