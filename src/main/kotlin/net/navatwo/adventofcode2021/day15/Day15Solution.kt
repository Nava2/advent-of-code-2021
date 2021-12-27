package net.navatwo.adventofcode2021.day15

import net.navatwo.adventofcode2021.Coord
import net.navatwo.adventofcode2021.coords
import net.navatwo.adventofcode2021.framework.ComputedResult
import net.navatwo.adventofcode2021.framework.Solution
import net.navatwo.adventofcode2021.get
import net.navatwo.adventofcode2021.set

private const val RESET = "\u001b[0m" // Text Reset
private const val PURPLE_BOLD = "\u001b[1;35m" // PURPLE
private const val GREY = "\u001b[0;37m" // GREY
private const val WHITE = "\u001b[1;37m" // WHITE

sealed class Day15Solution : Solution<Day15Solution.Input> {
    object Part1 : Day15Solution() {
        override fun parse(lines: List<String>): Input {
            return Input(
                board = lines.map { line ->
                    line.map { r -> RiskLevel(r.digitToInt()) }
                }
            )
        }

        override fun solve(input: Input): ComputedResult {
            val board = input.board

            val path = djikstra(board)

//            println(writeRiskBoard(board, path))

            val result = path.fold(0L) { acc, coord -> acc + board[coord].risk }
            return ComputedResult.Simple(result)
        }
    }

    object Part2 : Day15Solution() {
        override fun parse(lines: List<String>): Input {
            val board = lines.map { line ->
                line.map { r -> RiskLevel(r.digitToInt()) }
            }
            return Input(
                board = loadBoard(board)
            )
        }

        override fun solve(input: Input): ComputedResult {
            val board = input.board

            val path = djikstra(board)

//            println(writeRiskBoard(board, path))

            val result = path.fold(0L) { acc, coord -> acc + board[coord].risk }
            return ComputedResult.Simple(result)
        }

        private fun loadBoard(board: List<List<RiskLevel>>): List<List<RiskLevel>> {
            val newBoard = MutableList(board.size * 5) { y ->
                val projY = y.mod(board.size)

                val row = board[projY]
                ArrayList<RiskLevel>(row.size * 5).apply {
                    repeat(row.size * 5) { add(RiskLevel(0)) }
                }
            }

            for (coord in board.coords()) {
                newBoard[coord.y][coord.x] = board[coord.y][coord.x]
            }

            // 1 2 3 4
            // 2 3 4 5
            // 3 4 5 6
            // 4 5 6 7
            for (y in newBoard.indices) {
                val projY = y.mod(board.size)
                val copiedY = y / board.size
                val prevY = if (copiedY == 0) y else y - board.size

                val row = board[projY]

                val newRow = newBoard[y]
                for (x in newRow.indices) {
                    if (x in row.indices && y in board.indices) continue

                    val copiedX = x / row.size
                    val prevX = if (copiedX == 0) x else x - row.size

                    val nextValue = maxOf(
                        1,
                        (newBoard[y][prevX].risk + 1).mod(10),
                        (newBoard[prevY][x].risk + 1).mod(10),
                    )

                    newBoard[y][x] = RiskLevel(nextValue)
                }
            }

            return newBoard
        }

    }

    data class Input(
        val board: List<List<RiskLevel>>,
    )

    @JvmInline
    value class RiskLevel(val risk: Int)

    internal fun djikstra(board: List<List<RiskLevel>>): List<Coord> {
        val rowSize = board.first().size
        val coordsToVisit = board.indices
            .flatMap { y ->
                board.first().indices.map { x -> Coord(x, y) }
            }
            .toMutableSet()

        val risks = Array(board.size) {
            IntArray(rowSize) { Int.MAX_VALUE }
        }

        val start = Coord(0, 0)
        val end = Coord(board.first().lastIndex, board.lastIndex)

        risks[start] = 0

        val queue = ArrayDeque<Coord>()
        queue.add(start)

        val prevMap = mutableMapOf<Coord, Coord>()

        while (queue.isNotEmpty()) {
            val current = queue.minByOrNull { risks[it] }!!
            while (queue.remove(current)) {
                // remove all the elements
            }

            if (!coordsToVisit.remove(current)) continue

            val neighbours = current.nextCoords()
                .filter { it.x >= 0 && it.y >= 0 && it.x <= board.first().lastIndex && it.y <= board.lastIndex }
                .filter { it in coordsToVisit }

            val currentRisk = risks[current]
            for (neighbour in neighbours) {
                val altRisk = currentRisk + board[neighbour].risk
                if (altRisk < risks[neighbour]) {
                    risks[neighbour] = altRisk
                    prevMap[neighbour] = current
                }
            }

            queue.addAll(neighbours)
        }

        val path = ArrayList<Coord>(board.size + board.first().size)
        var next = end

        while (next != start) {
            path.add(next)
            next = prevMap.getValue(next)
        }

//        println(writeBoard(risks.map { r -> r.map { it } }, path))

        return path
    }

    internal fun writeRiskBoard(newBoard: List<List<RiskLevel>>, path: List<Coord>): String {
        return writeBoard(newBoard.map { r -> r.map { it.risk } }, path)
    }

    internal fun writeBoard(newBoard: List<List<Int>>, path: List<Coord>): String {
        val pathSet = path.toSet()

        val maxWidth = newBoard.maxOf { r -> r.maxOf { it.toString().length } }

        val sb = StringBuilder()
        sb.append(RESET)

        for (y in newBoard.indices) {
            val newRow = newBoard[y]

            for (x in newRow.indices) {
//                    if (x.mod(10) == 0) {
//                        sb.append(' ')
//                    }

                sb.append(' ')
                sb.append(if (x.mod(2) == 0) GREY else WHITE)

                if (Coord(x, y) in pathSet) {
                    sb.append(PURPLE_BOLD)
                }

                sb.append(newBoard[y][x].toString().padStart(maxWidth, '0'))
            }

            sb.appendLine()
//                if (y.mod(10) == 0) {
//                    sb.appendLine()
//                }
        }

        return sb.append(RESET).toString()
    }

    private fun Coord.nextCoords(): List<Coord> {
        return listOf(
            Coord(x - 1, y),
            Coord(x, y - 1),
            Coord(x + 1, y),
            Coord(x, y + 1),
        )
    }
}