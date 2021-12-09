package net.navatwo.adventofcode2021.benchmarks

import net.navatwo.adventofcode2021.framework.ComputedResult
import net.navatwo.adventofcode2021.framework.Solution
import kotlin.system.measureTimeMillis
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class Benchmark<I, R : ComputedResult> private constructor(
    private val inputContent: List<String>,
    private val solution: Solution<I, R>,
    private val inputConfig: Config?,
    private val solveConfig: Config,
) {
    private val inputs = solution.parse(inputContent)

    fun run() {
        inputConfig?.runBenchmark {
            solution.parse(inputContent)
        }

        solveConfig.runBenchmark {
            solution.solve(inputs)
        }
    }

    companion object {
        fun <I, R : ComputedResult> run(
            inputContent: List<String>,
            solution: Solution<I, R>,
            inputConfig: Config? = Stage.INPUT.config(warmupIterations = 2000U, iterations = 25_000U),
            solveConfig: Config = Stage.SOLVE.config(warmupIterations = 5000U, iterations = 25_000U),
        ) {
            val benchmark = Benchmark(
                inputContent = inputContent,
                solution = solution,
                inputConfig = inputConfig,
                solveConfig = solveConfig,
            )

            benchmark.run()
        }
    }

    enum class Stage {
        INPUT,
        SOLVE,
        ;

        fun config(warmupIterations: UInt, iterations: UInt): Config = Config(
            stage = this,
            warmupIterations = warmupIterations.toInt(),
            iterations = iterations.toInt(),
        )
    }

    data class Config internal constructor(
        val stage: Stage,
        val warmupIterations: Int,
        val iterations: Int,
    )

    class Result(
        val stage: Stage,
        val totalDuration: Duration,
        val operationDuration: Duration,
        val iterations: Int,
    )
}

private inline fun Benchmark.Config.runBenchmark(operation: () -> Unit): Benchmark.Result {
    println("[$stage] Warming up [$warmupIterations ops]")
    val warmupTime = measureTimeMillis {
        repeat(warmupIterations) {
            operation()
        }
    }.toDuration(DurationUnit.MILLISECONDS)

    println("[$stage] Completed [$warmupTime, ${warmupTime / warmupIterations.toDouble()}/op]")

    println("[$stage] Running [$iterations ops]")
    val solveTime = measureTimeMillis {
        repeat(iterations) {
            operation()
        }
    }.toDuration(DurationUnit.MILLISECONDS)

    val operationTime = solveTime / iterations.toDouble()
    println("[$stage] Completed operations [$solveTime, $operationTime/op]")

    return Benchmark.Result(
        stage = stage,
        totalDuration = solveTime,
        operationDuration = operationTime,
        iterations = iterations,
    )
}
