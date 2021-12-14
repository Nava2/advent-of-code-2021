package net.navatwo.adventofcode2021.day13

import net.navatwo.adventofcode2021.Coord
import net.navatwo.adventofcode2021.framework.ComputedResult
import net.navatwo.adventofcode2021.framework.Solution

sealed class Day13Solution : Solution<Day13Solution.Input> {
    object Part1 : Day13Solution() {
        override fun solve(input: Input): ComputedResult {
            TODO("foo bar")
        }
    }

    object Part2 : Day13Solution() {
        override fun solve(input: Input): ComputedResult {
            TODO()
        }
    }

    override fun parse(lines: List<String>): Input {
        val points = mutableListOf<Coord>()

        val linesIterator = lines.iterator()
        do {
            val line = linesIterator.next()
            if (line.isEmpty()) break

            val (x, y) = line.split(',').map { it.toInt() }
            points.add(Coord(x = x, y = y))
        } while (linesIterator.hasNext())

        val folds = mutableListOf<Fold>()
        while (linesIterator.hasNext()) {
            val line = linesIterator.next()
            val (direction, value) = line.substring("fold along ".length, line.length).split('=')

            folds.add(
                when (direction) {
                    "x" -> Fold.X(value.toInt())
                    "y" -> Fold.Y(value.toInt())
                    else -> error("bad input")
                }
            )
        }

        return Input(points, folds)
    }

    data class Input(
        val points: List<Coord>,
        val folds: List<Fold>,
    )

    sealed class Fold {
        abstract val line: Int

        data class X(override val line: Int) : Fold
        data class Y(override val line: Int) : Fold
    }
}