package net.navatwo.adventofcode2021.day5

import net.navatwo.adventofcode2021.framework.Solution
import kotlin.math.sign

class Day5Solution private constructor(
    private val includeDiagonals: Boolean
) : Solution<Day5Solution.Input, Day5Solution.Result> {
    companion object {
        val Part1 = Day5Solution(false)
        val Part2 = Day5Solution(true)
    }

    override fun parse(lines: List<String>): Input {
        return Input(
            lines = lines.map { line ->
                val (from, to) = line.split("->")
                Input.Line(
                    from = Point.parse(from),
                    to = Point.parse(to),
                )
            }
        )
    }

    override fun solve(input: Input): Result {
        val lines = run {
            var sequence = input.lines.asSequence()
                .map { (from, to) -> Line(from, to) }

            if (!includeDiagonals) {
                sequence = sequence.filter { it.rate.isPerpendicular }
            }

            sequence.toList()
        }

        val maxPoints = lines.fold(Point(0, 0)) { acc, line ->
            var result = acc

            line.forEachPoint { point ->
                result = maxOfElements(point, result)
            }

            result
        }

        val board = Array(maxPoints.y + 1) { IntArray(maxPoints.x + 1) }

        for (line in lines) {
            line.forEachPoint { point ->
                val row = board[point.y]
                row[point.x] += 1
            }
        }

        return Result(
            computed = board.sumOf { r ->
                r.count { it >= 2 }
            }
        )
    }

    data class Line(
        val from: Point,
        val to: Point,
    ) {
        val rate: Rate = computeRate()

        inline fun forEachPoint(pointFn: (Point) -> Unit) {
            var point = from
            pointFn(point)

            while (point != to) {
                point += rate
                pointFn(point)
            }
        }

        private fun computeRate(): Rate {
            val xRate = to.x - from.x
            val yRate = to.y - from.y
            return Rate(xRate.sign, yRate.sign)
        }
    }

    data class Rate(val x: Int, val y: Int) {
        val isPerpendicular = x == 0 || y == 0
    }

    data class Point(val x: Int, val y: Int) {
        operator fun plus(rate: Rate): Point {
            return Point(x + rate.x, y + rate.y)
        }

        companion object {
            fun parse(input: String): Point {
                val (x, y) = input.trim().split(',')
                return Point(x = x.toInt(), y = y.toInt())
            }
        }
    }

    fun maxOfElements(a: Point, b: Point): Point {
        return Point(x = maxOf(a.x, b.x), y = maxOf(a.y, b.y))
    }

    data class Input(
        val lines: List<Line>,
    ) {
        data class Line(
            val from: Point,
            val to: Point,
        )
    }

    data class Result(val computed: Int)
}