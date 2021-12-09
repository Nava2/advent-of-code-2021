package net.navatwo.adventofcode2021.day7

import net.navatwo.adventofcode2021.framework.ComputedResult
import net.navatwo.adventofcode2021.framework.Solution
import kotlin.math.absoluteValue

sealed class Day7Solution : Solution<Day7Solution.Input, Day7Solution.Result> {
    object Part1 : Day7Solution() {
        override fun computeFuelCostBetween(from: Int, distance: Int): Long = distance.toLong()
    }

    object Part2 : Day7Solution() {
        override fun computeFuelCostBetween(from: Int, distance: Int): Long {
            return when (val n = distance.toLong()) {
                0L -> 0L
                1L -> 1L
                else -> ((n + 1) * n) / 2
            }
        }
    }

    override fun parse(lines: List<String>): Input {
        return Input(
            crabs = lines.first().splitToSequence(',').map { Crab(it.toInt()) }.toList(),
        )
    }

    override fun solve(input: Input): Result {
        val crabTable = CrabTable.load(input.crabs)

        val maxPosition = crabTable.crabs.maxOf { it.crab.horizontalPosition }
        val distanceCosts = LongArray(maxPosition + 1)

        var minimumPosition = Int.MAX_VALUE
        var minimumCost = Long.MAX_VALUE
        for (position in 0..maxPosition) {
            val currentCost = computeFuelCostTo(distanceCosts, crabTable, position)
            if (currentCost < minimumCost) {
                minimumCost = currentCost
                minimumPosition = position
            }
        }

        return Result(
            position = minimumPosition,
            computed = minimumCost,
        )
    }

    abstract fun computeFuelCostBetween(from: Int, distance: Int): Long

    data class Input(
        val crabs: List<Crab>,
    )

    data class Result(
        val position: Int,
        override val computed: Long
    ) : ComputedResult

    @JvmInline
    value class Crab(val horizontalPosition: Int)

    @JvmInline
    value class CrabTable(val crabs: List<CrabWithCount>) {

        data class CrabWithCount(val crab: Crab, val count: Int)

        companion object {
            fun load(crabs: List<Crab>): CrabTable {
                return CrabTable(
                    crabs = crabs.groupBy { it }
                        .mapValues { (_, v) -> v.size }
                        .entries
                        .map { (crab, count) -> CrabWithCount(crab, count) }
                )
            }
        }
    }


    fun computeFuelCostTo(
        distanceCosts: LongArray,
        crabTable: CrabTable,
        position: Int,
    ): Long {
        return crabTable.crabs.sumOf { (crab, count) ->
            val from = crab.horizontalPosition
            val distance = (from - position).absoluteValue

            val computedDistance = distanceCosts[distance]
            if (computedDistance != 0L || distance == 0) {
                count * computedDistance
            } else {
                val fuelCost = computeFuelCostBetween(from, distance)
                distanceCosts[distance] = fuelCost
                count * fuelCost
            }
        }
    }
}