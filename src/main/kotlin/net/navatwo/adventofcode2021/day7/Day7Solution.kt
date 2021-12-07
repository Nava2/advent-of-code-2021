package net.navatwo.adventofcode2021.day7

import net.navatwo.adventofcode2021.framework.ComputedResult
import net.navatwo.adventofcode2021.framework.Solution
import kotlin.math.absoluteValue

sealed class Day7Solution : Solution<Day7Solution.Input, Day7Solution.Result> {
    object Part1 : Day7Solution() {
        override fun computeFuelCostBetween(from: Int, distance: Int): Long = distance.toLong()

        override fun loadDistances(distanceCosts: LongArray) {
            for (i in distanceCosts.indices) {
                distanceCosts[i] = i.toLong()
            }
        }
    }

    object Part2 : Day7Solution() {
        override fun computeFuelCostBetween(from: Int, distance: Int): Long {
            return when (val n = distance.toLong()) {
                0L -> 0L
                1L -> 1L
                else -> ((n + 1) * n) / 2
            }
        }

        override fun loadDistances(distanceCosts: LongArray) {
            distanceCosts[0] = 0
            distanceCosts[1] = 1

            var prev = 1L
            for (i in distanceCosts.indices.drop(2)) {
                val cost = prev + i.toLong()
                distanceCosts[i] = cost
                prev = cost
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

        val maxPosition = crabTable.positions().last
        val distanceCosts = LongArray(maxPosition + 1)
        loadDistances(distanceCosts)

        var minimumPosition = Int.MAX_VALUE
        var minimumCost = Long.MAX_VALUE
        for (position in crabTable.positions()) {
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

    abstract fun loadDistances(distanceCosts: LongArray)

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
    value class CrabTable(val crabPositions: LongArray) {
        fun positions() = crabPositions.indices

        inline fun countAt(position: Int) = crabPositions[position]

        companion object {
            fun load(crabs: List<Crab>): CrabTable {
                val maxPosition = crabs.maxOf { it.horizontalPosition }
                val crabPositions = LongArray(maxPosition + 1)
                for (crab in crabs) {
                    crabPositions[crab.horizontalPosition] += 1L
                }

                return CrabTable(crabPositions)
            }
        }
    }


    fun computeFuelCostTo(
        distanceCosts: LongArray,
        crabTable: CrabTable,
        position: Int,
    ): Long {
        return crabTable.crabPositions.asSequence().withIndex().sumOf { (crabPosition, count) ->
            val distance = (crabPosition - position).absoluteValue

            distanceCosts[distance] * count

//            val memoizedDistance = distanceCosts[distance]
//            if (memoizedDistance != 0L) {
//                memoizedDistance
//            } else {
//                val fuelCost = computeFuelCostBetween(from = crabPosition, distance)
//                distanceCosts[distance] = fuelCost
//                count * fuelCost
//            }
        }
    }
}