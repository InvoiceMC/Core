package me.outspending.core.benchmarks

import me.outspending.core.utils.Utilities.toTinyString
import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.annotations.BenchmarkMode
import org.openjdk.jmh.annotations.Fork
import org.openjdk.jmh.annotations.Mode

@BenchmarkMode(Mode.AverageTime)
@Fork(value = 1, warmups = 2)
open class FormattingBenchmarks {

    @Benchmark
    fun epicness() {
        val string: String = "this is something".toTinyString()
    }
}