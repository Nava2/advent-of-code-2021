package net.navatwo.adventofcode2021.day13

import net.navatwo.adventofcode2021.Coord
import net.navatwo.adventofcode2021.framework.ComputedResult
import net.navatwo.adventofcode2021.framework.Solution

sealed class Day13Solution : Solution<Day13Solution.Input> {
    object Part1 : Day13Solution() {
        override fun solve(input: Input): ComputedResult {
            val pointsSet = input.points.toSet()
            val initialCard = MutableList(input.boundary.y + 1) { y ->
                MutableList(input.boundary.x + 1) { x ->
                    Coord(x = x, y = y) in pointsSet
                }
            }

            val card = input.folds.take(1).fold(initialCard) { card, fold ->
                when (fold) {
                    is Fold.X -> {
                        val rowsToFoldLeft = MutableList(card.size) { i -> card[i].subList(0, fold.x) }
                        val rowsToFoldRight = List(card.size) { i ->
                            card[i].subList(fold.x + 1, card[i].size).reversed()
                        }

                        for ((into, from) in rowsToFoldLeft.zip(rowsToFoldRight)) {
                            for ((idx, ic) in from.withIndex()) {
                                into[idx] = into[idx] || ic
                            }
                        }

                        rowsToFoldLeft
                    }
                    is Fold.Y -> {
                        val rowsToFoldUp = card.subList(fold.y + 1, card.size).reversed()
                        val rowsToFoldInto = card.subList(fold.y - rowsToFoldUp.size, fold.y)
                        for ((into, from) in rowsToFoldInto.zip(rowsToFoldUp)) {
                            for ((idx, ic) in from.withIndex()) {
                                into[idx] = into[idx] || ic
                            }
                        }

                        rowsToFoldInto
                    }
                }
            }

            return ComputedResult.Simple(card.sumOf { it.count { b -> b } })
        }
    }

    object Part2 : Day13Solution() {
        override fun solve(input: Input): ComputedResult {
            TODO()
        }
    }

    override fun parse(lines: List<String>): Input {
        val points = mutableListOf<Coord>()
        var boundary = Coord(0, 0)

        val linesIterator = lines.iterator()
        do {
            val line = linesIterator.next()
            if (line.isEmpty()) break

            val (x, y) = line.split(',').map { it.toInt() }

            points.add(Coord(x = x, y = y))
            boundary = Coord(x = maxOf(boundary.x, x), y = maxOf(boundary.y, y))
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

        return Input(boundary, points, folds)
    }

    data class Input(
        val boundary: Coord,
        val points: List<Coord>,
        val folds: List<Fold>,
    )

    sealed interface Fold {
        data class X(val x: Int) : Fold
        data class Y(val y: Int) : Fold
    }
}