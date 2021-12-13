package net.navatwo.adventofcode2021.day11

import net.navatwo.adventofcode2021.Coord
import net.navatwo.adventofcode2021.coords
import net.navatwo.adventofcode2021.framework.ComputedResult
import net.navatwo.adventofcode2021.framework.Solution
import net.navatwo.adventofcode2021.get
import net.navatwo.adventofcode2021.getOrNull
import net.navatwo.adventofcode2021.initializeVisitedGrid
import net.navatwo.adventofcode2021.resetVisited
import net.navatwo.adventofcode2021.set

private val MAX_ENERGY = 9

sealed class Day11Solution : Solution<Day11Solution.Input> {
    object Part1 : Day11Solution() {
        override fun solve(input: Input): ComputedResult {
            val octopuses = input.octopuses
                .map { it.toMutableList() }
                .toMutableList()

            val octopusQueue = ArrayDeque<Coord>()

            val flashed = input.octopuses.initializeVisitedGrid()

            val counter = (1..100).sumOf {
                val count = runStep(octopuses, flashed, octopusQueue)

                flashed.resetVisited()

                count
            }

            return ComputedResult.Simple(counter)
        }
    }

    object Part2 : Day11Solution() {
        override fun solve(input: Input): ComputedResult {
            val octopuses = input.octopuses
                .map { it.toMutableList() }
                .toMutableList()

            val octopusQueue = ArrayDeque<Coord>()

            val flashed = input.octopuses.initializeVisitedGrid()

            var step = 0
            do {
                flashed.resetVisited()

                step += 1

                runStep(octopuses, flashed, octopusQueue)
            } while (flashed.any { it.any { b -> !b } })

            return ComputedResult.Simple(step)
        }
    }

    override fun parse(lines: List<String>): Input {
        return Input(
            octopuses = lines.map { line ->
                line.map { Octopus(it.digitToInt()) }
            },
        )
    }

    data class Input(
        val octopuses: List<List<Octopus>>,
    )

    @JvmInline
    value class Octopus(val energy: Int) {
        fun inc(): Octopus {
            return Octopus(energy + 1)
        }
    }

    internal fun forCoords(coord: Coord): Iterable<Coord> {
        return (-1..+1).flatMap { yOffset ->
            (-1..+1).map { xOffset ->
                Coord(coord.x + xOffset, coord.y + yOffset)
            }
        }
    }

    internal fun runStep(
        octopuses: MutableList<MutableList<Octopus>>,
        flashed: Array<BooleanArray>,
        octopusQueue: ArrayDeque<Coord>,
    ): Long {
        // Run a step
        // first increment all by 1
        var counter = 0L
        for (coord in octopuses.coords()) {
            val octopus = octopuses[coord]
            octopuses[coord] = octopus.inc()
        }

        octopusQueue.addAll(octopuses.getCoordsToFlash())

        while (octopusQueue.isNotEmpty()) {
            val coord = octopusQueue.removeFirst()
            if (flashed[coord]) continue

            counter += 1
            flashed[coord] = true

            for (coordToInc in forCoords(coord).filter { it != coord && flashed.getOrNull(it) == false }) {
                val octopus = octopuses.getOrNull(coordToInc) ?: continue

                val incOctopus = octopus.inc()
                octopuses[coordToInc] = incOctopus
                if (incOctopus.energy > MAX_ENERGY) {
                    // flash it then
                    octopusQueue.add(coordToInc)
                }
            }
        }

        for (coord in octopuses.coords().filter { flashed[it] }) {
            octopuses[coord] = Octopus(0)
        }

        return counter
    }

    fun List<List<Octopus>>.getCoordsToFlash(): Sequence<Coord> {
        return coords().filter { this[it].energy > MAX_ENERGY }
    }
}