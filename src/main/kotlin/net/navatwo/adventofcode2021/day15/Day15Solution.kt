package net.navatwo.adventofcode2021.day15

import net.navatwo.adventofcode2021.Coord
import net.navatwo.adventofcode2021.framework.ComputedResult
import net.navatwo.adventofcode2021.framework.Solution
import net.navatwo.adventofcode2021.get
import net.navatwo.adventofcode2021.set

sealed class Day15Solution : Solution<Day15Solution.Input> {
    object Part1 : Day15Solution() {
        override fun solve(input: Input): ComputedResult {
            val board = input.board

            val rowSize = board.first().size
            val visits = Array(board.size) {
                BooleanArray(rowSize)
            }

            val costs = Array(board.size) {
                IntArray(rowSize) { Int.MAX_VALUE }
            }

            val start = Coord(0, 0)
            val end = Coord(board.first().lastIndex, board.lastIndex)

            val queue = ArrayDeque<Coord>()
            queue.add(start)

            val prevMap = mutableMapOf<Coord, Coord>()

            while (queue.isNotEmpty()) {
                val current = queue.removeFirst()

                val visited = visits[current]
                if (visited) continue

                val neighbours = current.nextCoords()
                    .filter { it.x >= 0 && it.y >= 0 && it.x <= costs.first().lastIndex && it.y <= costs.lastIndex }
                    .filter { !visits[it] }

                val currentCost = costs[current]
                for (neighbour in neighbours) {
                    val altCost = currentCost + board[neighbour].risk
                    if (altCost < costs[neighbour]) {
                        costs[neighbour] = altCost
                        prevMap[neighbour] = current
                    }
                }

                visits[current] = true
                queue.addAll(neighbours)

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