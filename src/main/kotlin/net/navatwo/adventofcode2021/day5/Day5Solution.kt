package net.navatwo.adventofcode2021.day5

import net.navatwo.adventofcode2021.day5.Day5Solution.Input.Point
import net.navatwo.adventofcode2021.framework.Solution
import kotlin.math.sign

private val memoizedGcd = mutableMapOf<Pair<Int, Int>, Int>()

fun gcd(in1: Int, in2: Int): Int {
    // Always set to positive
    val an1 = if (in1 > 0) in1 else -in1
    val an2 = if (in2 > 0) in2 else -in2

    if (an1 == an2) return an1

    return memoizedGcd.computeIfAbsent(an1 to an2) { (in1, in2) ->
        var n1 = in1
        var n2 = in2
        while (n1 != n2) {
            if (n1 > n2) {
                n1 -= n2
            } else {
                n2 -= n1
            }
        }

        n1
    }
}

sealed class Day5Solution<S> : Solution<Day5Solution.Input, S> {
    object Part1 : Day5Solution<Result>() {
        override fun solve(input: Input): Result {

            val lines = input.lines.asSequence()
                .map { (from, to) -> Line(from, to) }
                .filter { it.rate.isPerpendicular }
                .toList()

            val maxPoints = lines.fold(Point(0, 0)) { acc, line ->
                var result = acc

                line.forEachPoint { point ->
                    result = Point(
                        x = maxOf(point.x, result.x),
                        y = maxOf(point.y, result.y),
                    )
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
    }

    object Part2 : Day5Solution<Result>() {
        override fun solve(input: Input): Result {
            TODO()
        }
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

    data class Line(
        val from: Point,
        val to: Point,
    ) {
        val rate: Rate = computeRate()

        inline fun forEachPoint(pointFn: (Point) -> Unit) {
            var point = from
            pointFn(point)

            while (point != to) {
                point = Point(point.x + rate.x, point.y + rate.y)
                pointFn(point)
            }
        }

        companion object {
            private fun Line.computeRate(): Rate {
                val xRate = to.x - from.x
                val yRate = to.y - from.y

                return if (xRate == 0) {
                    Rate(x = 0, y = yRate.sign)
                } else if (yRate == 0) {
                    Rate(x = xRate.sign, y = 0)
                } else {
                    val multiplier = gcd(xRate, yRate)
                    Rate(xRate / multiplier, yRate / multiplier)
                }
            }
        }
    }

    data class Rate(val x: Int, val y: Int) {
        val isPerpendicular = x == 0 || y == 0
    }

    data class Input(
        val lines: List<Line>,
    ) {
        data class Line(
            val from: Point,
            val to: Point,
        )

        data class Point(val x: Int, val y: Int) {
            companion object {
                fun parse(input: String): Point {
                    val (x, y) = input.trim().split(',')
                    return Point(x = x.toInt(), y = y.toInt())
                }
            }
        }
    }

    data class Result(val computed: Int)
}