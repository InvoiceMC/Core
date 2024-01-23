package me.outspending.core.utils.helpers

import kotlin.math.floor
import kotlin.math.log10
import kotlin.math.pow

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

class NumberHelper(
    private val number: Number
) {

    // Separate thousands with commas
    fun toCommas() = String.format("%,d", number)

    // Convert number to 1K, 1M, 1B, 1T, etc.
    fun toShorten(): String {
        val numValue: Long = number.toLong()
        val value = floor(log10(numValue.toDouble())).toInt()
        val base = value / 3
        if (value >= 3 && base < FORMAT_SUFFIXES.size) {
            val formattedNumber = numValue / 10.0.pow((base * 3).toDouble())
            return if (formattedNumber % 1 == 0.0) {
                String.format("%.0f%s", formattedNumber, FORMAT_SUFFIXES[base])
            } else {
                String.format("%.1f%s", formattedNumber, FORMAT_SUFFIXES[base])
            }
        } else {
            return String.format("%.0f", numValue.toDouble())
        }
    }

    // Convert number to a bar like this: <gray>[<main>▐▐▐▐<gray>▐▐▐]
    fun toBar(max: Number, bars: Int, separator: String = "|"): String {
        val filledBars = (number.toDouble() / max.toDouble() * bars.toDouble()).toInt()
        val emptyBars = bars - filledBars
        return "<main>[<main>${separator.repeat(filledBars)}<gray>${separator.repeat(emptyBars)}<main>]"
    }

    // Convert number to tiny numbers
    fun toTinyNumbers() = number.toString().map { TINY_NUMBERS[it] ?: it }.joinToString("")
}