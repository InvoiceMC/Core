package me.outspending.core.utils.helpers

class TimerHelper {

    private val startTime = System.nanoTime()

    fun elapsed() = (System.nanoTime() - startTime) // in millis

    fun formattedElapsed(): String {
        val millis = elapsed()
        val micros = millis * 1000.0
        val seconds = millis / 1000.0
        val minutes = seconds / 60.0
        val hours = minutes / 60.0

        return when {
            millis < 1 -> "%.2f µs".format(micros)
            millis < 1000 -> "%.2f ms".format(millis)
            seconds < 60 -> "%.2f s".format(seconds)
            minutes < 60 -> "%.2f m".format(minutes)
            else -> "%.2f h".format(hours)
        }
    }
}