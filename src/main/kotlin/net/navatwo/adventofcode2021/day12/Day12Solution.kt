package net.navatwo.adventofcode2021.day12

import net.navatwo.adventofcode2021.framework.ComputedResult
import net.navatwo.adventofcode2021.framework.Solution

private val START = Day12Solution.Cave("start")
private val END = Day12Solution.Cave("end")

sealed class Day12Solution : Solution<Day12Solution.Input> {
    object Part1 : Day12Solution() {
        override fun solve(input: Input): ComputedResult {
            val connections = buildConnections(input)

            val initialPath = Path(listOf(START))
            val paths = mutableSetOf<Path>()
            helper(
                connections = connections,
                paths = paths,
                caveCounts = connections.mapValues { (cave, _) ->
                    if (cave != START) 0 else 1
                },
                currentPath = initialPath,
            )

            return ComputedResult.Simple(paths.size)
        }

        private fun helper(
            connections: Map<Cave, List<Cave>>,
            paths: MutableSet<Path>,
            caveCounts: Map<Cave, Int>,
            currentPath: Path,
        ) {
            val lastCave = currentPath.caves.last()
            if (lastCave == END) {
                paths.add(currentPath)
                return
            }

            val nextCaves = connections[lastCave]
                ?.filter { nextCave ->
                    if (nextCave == START) return@filter false
                    caveCounts.getValue(nextCave) == 0 || nextCave.isLarge()
                }
                ?: return

            for (nextCave in nextCaves) {
                helper(
                    connections = connections,
                    paths = paths,
                    caveCounts = caveCounts + mapOf(nextCave to 1),
                    currentPath = currentPath.append(nextCave),
                )
            }
        }
    }

    object Part2 : Day12Solution() {
        override fun solve(input: Input): ComputedResult {
            val connections = buildConnections(input)

            val initialPath = Path(listOf(START))
            val paths = mutableSetOf<Path>()
            helper(
                connections = connections,
                paths = paths,
                caveCounts = connections.mapValues { (cave, _) ->
                    if (cave != START) 0 else 1
                },
                currentPath = initialPath,
                twoVisitCave = null,
            )

            return ComputedResult.Simple(paths.size)
        }

        private fun helper(
            connections: Map<Cave, List<Cave>>,
            paths: MutableSet<Path>,
            caveCounts: Map<Cave, Int>,
            currentPath: Path,
            twoVisitCave: Cave?,
        ) {
            val lastCave = currentPath.caves.last()
            if (lastCave == END) {
                paths.add(currentPath)
                return
            }

            val nextCaves = connections[lastCave]
                ?.filter { nextCave ->
                    if (nextCave == START) return@filter false

                    val caveCount = caveCounts.getValue(nextCave)
                    when {
                        nextCave.isLarge() -> true
                        caveCount == 0 -> true
                        twoVisitCave == nextCave && caveCount <= 1 -> true
                        else -> false
                    }
                }
                ?: return

            for (nextCave in nextCaves) {
                val currentCount = caveCounts.getValue(nextCave)
                if (twoVisitCave == null && currentCount == 0 && !nextCave.isLarge()) {
                    helper(
                        connections = connections,
                        paths = paths,
                        caveCounts = caveCounts + mapOf(nextCave to 1),
                        currentPath = currentPath.append(nextCave),
                        twoVisitCave = nextCave,
                    )
                }

                helper(
                    connections = connections,
                    paths = paths,
                    caveCounts = caveCounts + mapOf(nextCave to currentCount + 1),
                    currentPath = currentPath.append(nextCave),
                    twoVisitCave = twoVisitCave,
                )
            }
        }
    }


    override fun parse(lines: List<String>): Input {
        return Input(
            connections = lines.map { line ->
                val (a, b) = line.split('-')
                Connection(Cave(a), Cave(b))
            }
        )
    }

    internal fun buildConnections(input: Input) = input.connections
        .flatMap {
            listOf(it.a to it.b, it.b to it.a)
        }
        .groupBy({ it.first }) { it.second }

    data class Input(
        val connections: List<Connection>,
    )

    data class Connection(val a: Cave, val b: Cave)

    @JvmInline
    value class Cave(val name: String) {
        fun isLarge(): Boolean = name[0].isUpperCase()
    }

    @JvmInline
    value class Path(val caves: List<Cave>) {
        fun append(cave: Cave): Path = Path(caves + cave)
    }
}