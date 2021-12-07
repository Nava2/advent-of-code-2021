package net.navatwo.adventofcode2021.day6

import net.navatwo.adventofcode2021.day6.Day6Solution.Result.FishTable
import net.navatwo.adventofcode2021.framework.Solution

const val NEW_BIRTH_DAYS = 8
const val BUFFER_COUNT = NEW_BIRTH_DAYS + 1

sealed class Day6Solution : Solution<Day6Solution.Input, Day6Solution.Result> {
    object Part1 : Day6Solution() {
        override fun solve(input: Input): Result {
            return computeFish(input, 80)
        }
    }

    object Part2 : Day6Solution() {
        override fun solve(input: Input): Result {
            return computeFish(input, 256)
        }
    }

    override fun parse(lines: List<String>): Input {
        return Input(
            fish = lines.first().splitToSequence(',')
                .map { AnglerFish(timeToSpawn = it.toInt()) }
                .toList(),
        )
    }

    @JvmInline
    value class AnglerFish(val timeToSpawn: Int)

    data class Input(
        val fish: List<AnglerFish>,
    )

    data class Result(
        val fishTable: FishTable,
        val computed: Long,
    ) {
        data class FishTable(
            val fish: List<Long>,
        ) {
            fun fishes(): Iterable<AnglerFish> {
                return fish.asSequence()
                    .withIndex()
                    .flatMap { (timeToSpawn, count) ->
                        (0 until count).asSequence().map { AnglerFish(timeToSpawn) }
                    }
                    .asIterable()
            }

            constructor(
                days: Int,
                fish: LongArray,
            ) : this(
                (days until days + BUFFER_COUNT).map { fish[it % BUFFER_COUNT] }
            )
        }
    }

    companion object {

        internal fun computeFish(input: Input, days: Int): Result {
            // each index is a day offset
            // each value is a count of fish
            val fish = LongArray(BUFFER_COUNT)
            for (f in input.fish) {
                fish[f.timeToSpawn] += 1L
            }

//            run {
//                val fishTable = FishTable(0, fish)
//                println("Initial state: " +
//                        fishTable.fishes().joinToString(",") { it.timeToSpawn.toString() })
//            }

            for (day in 0 until days) {
                val index = day % BUFFER_COUNT // index in the array
                val spawningToday = fish[index]
                fish[index] = 0

                val respawnIndex = (day + NEW_BIRTH_DAYS - 1) % BUFFER_COUNT
                fish[respawnIndex] += spawningToday

                val newSpawnIndex = (day + NEW_BIRTH_DAYS + 1) % BUFFER_COUNT
                fish[newSpawnIndex] += spawningToday

//                val fishTable = FishTable(day + 1, fish)
//                println("After ${(day + 1).toString().padStart(2, ' ')} days: " +
//                        fishTable.fishes().joinToString(",") { it.timeToSpawn.toString() })
            }

            return Result(
                fishTable = FishTable(days, fish),
                computed = fish.sumOf { it },
            )
        }
    }
}