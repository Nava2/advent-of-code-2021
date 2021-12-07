package net.navatwo.adventofcode2021.day7

import net.navatwo.adventofcode2021.framework.ComputedResult
import net.navatwo.adventofcode2021.framework.Solution
import kotlin.math.absoluteValue

sealed class Day7Solution : Solution<Day7Solution.Input, ComputedResult> {
    object Part1 : Day7Solution() {
        override fun solve(input: Input): ComputedResult {
            val crabTable = CrabTable.load(input.crabs)

            val positionToMoveTo = crabTable.positions().minByOrNull { position ->
                crabTable.computeFuelCostTo(position)
            }!!

            return ComputedResult.Simple(crabTable.computeFuelCostTo(position = positionToMoveTo))
        }
    }
    
    object Part2 : Day7Solution() {
        override fun solve(input: Input): ComputedResult {
            val crabTable = CrabTable.load(input.crabs)

            val positionToMoveTo = crabTable.positions().minByOrNull { position ->
                crabTable.computeFuelCostTo(position)
            }!!

            return ComputedResult.Simple(crabTable.computeFuelCostTo(position = positionToMoveTo))
        }
    }

    override fun parse(lines: List<String>): Input {
        return Input(
            crabs = lines.first().splitToSequence(',').map { Crab(it.toInt()) }.toList(),
        )
    }

    data class Input(
        val crabs: List<Crab>,
    )

    @JvmInline
    value class Crab(val horizontalPosition: Int)

    @JvmInline
    value class CrabTable(private val crabPositions: IntArray) {
        fun positionsAndCount(): Sequence<PositionAndCount> {
            return crabPositions.asSequence().withIndex().map { PositionAndCount(it) }
        }

        fun positions() = crabPositions.indices

        fun computeFuelCostBetween(from: Int, to: Int): Long = crabPositions[from].toLong() * (from - to).absoluteValue

        fun computeFuelCostTo(
            position: Int,
        ): Long {
            return positions().sumOf { crabPosition ->
                computeFuelCostBetween(crabPosition, position)
            }
        }

        @JvmInline
        value class PositionAndCount(
            private val indexedValue: IndexedValue<Int>,
        ) {
            val position: Int
                get() = indexedValue.index
            val count: Int
                get() = indexedValue.value

            operator fun component1(): Int = indexedValue.index
            operator fun component2(): Int = indexedValue.value
        }

        companion object {
            fun load(crabs: List<Crab>): CrabTable {
                val maxPosition = crabs.maxOf { it.horizontalPosition }
                val crabPositions = IntArray(maxPosition + 1)
                for (crab in crabs) {
                    crabPositions[crab.horizontalPosition] += 1
                }

                return CrabTable(crabPositions)
            }
        }
    }

    companion object {

    }
}