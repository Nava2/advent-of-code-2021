package net.navatwo.adventofcode2021.day15

import net.navatwo.adventofcode2021.Coord
import net.navatwo.adventofcode2021.framework.ComputedResult
import net.navatwo.adventofcode2021.framework.Solution
import net.navatwo.adventofcode2021.get
import net.navatwo.adventofcode2021.set
import java.util.PriorityQueue

sealed class Day15Solution : Solution<Day15Solution.Input> {
    object Part1 : Day15Solution() {
        override fun solve(input: Input): ComputedResult {
            val board = input.board

            val rowSize = board.first().size
            val visits = Array(board.size) {
                BooleanArray(rowSize)
            }

            val dists = Array(board.size) {
                IntArray(rowSize) { Int.MAX_VALUE }
            }

            val start = Coord(0, 0)
            dists[start] = 0

            val end = Coord(board.first().lastIndex, board.lastIndex)

            val queue = PriorityQueue<Coord>(compareBy { dists[it] })

            for (y in dists.indices) {
                for (x in dists.first().indices) {
                    val v = Coord(x, y)
                    if (v != start) {
                        dists[v] = Int.MAX_VALUE
                    }

                    queue.add(v)
                }
            }

            val prevMap = mutableMapOf<Coord, Coord>()

            while (queue.isNotEmpty()) {
                val current = queue.remove()

                val visited = visits[current]
                if (visited) continue

                val neighbours = current.nextCoords()
                    .filter { it.x >= 0 && it.y >= 0 && it.x <= dists.first().lastIndex && it.y <= dists.lastIndex }
                    .filter { !visits[it] }

                val currentCost = dists[current]
                for (neighbour in neighbours) {
                    val altCost = currentCost + board[neighbour].risk
                    if (altCost < dists[neighbour]) {
                        dists[neighbour] = altCost
                        prevMap[neighbour] = current

                        // update the priority
                        queue.remove(neighbour)
                        queue.add(neighbour)
                    }
                }

                visits[current] = true

                if (current == end) break
            }

            var result = 0L
            var next = end

            while (next != start) {
                result += board[next].risk
                next = prevMap.getValue(next)
            }

//            result += board[next].risk

            return ComputedResult.Simple(result)
        }

        fun Coord.nextCoords(): List<Coord> {
            return listOf(
                Coord(x - 1, y),
                Coord(x, y - 1),
                Coord(x + 1, y),
                Coord(x, y + 1),
            )
        }
    }

    object Part2 : Day15Solution() {
        override fun solve(input: Input): ComputedResult {
            TODO()
        }
    }

    override fun parse(lines: List<String>): Input {
        return Input(
            board = lines.map { line ->
                line.map { r -> RiskLevel(r.digitToInt()) }
            }
        )
    }

    data class Input(
        val board: List<List<RiskLevel>>,
    )

    @JvmInline
    value class RiskLevel(val risk: Int)
}