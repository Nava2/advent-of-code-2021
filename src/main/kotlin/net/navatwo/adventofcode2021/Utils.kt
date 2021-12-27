package net.navatwo.adventofcode2021

inline fun <T, R> Iterable<T>.mapToSet(transform: (T) -> R): Set<R> {
    return mapTo(mutableSetOf(), transform)
}

inline fun <T, R> Array<T>.mapToSet(transform: (T) -> R): Set<R> {
    return mapTo(mutableSetOf(), transform)
}

inline fun <T, R> Sequence<T>.mapToSet(transform: (T) -> R): Set<R> {
    return mapTo(mutableSetOf(), transform)
}

data class Coord(val x: Int, val y: Int)

fun List<List<*>>.coords(): Sequence<Coord> {
    val columnIndices = first().indices
    return indices.asSequence()
        .flatMap { y ->
            columnIndices.map { x -> Coord(x, y) }
        }
}

fun <T> List<List<T>>.getCoord(x: Int, y: Int): T? {
    return getOrNull(Coord(x, y))
}

operator fun <T> List<List<T>>.get(coord: Coord): T {
    val (x, y) = coord
    return this[y][x]
}

fun <T> List<List<T>>.getOrNull(coord: Coord): T? {
    val (x, y) = coord
    val row = getOrNull(y)
    return row?.getOrNull(x)
}

operator fun <T> MutableList<MutableList<T>>.set(coord: Coord, value: T) {
    this[coord.y][coord.x] = value
}

operator fun Array<BooleanArray>.get(coord: Coord): Boolean {
    return this[coord.y][coord.x]
}

operator fun Array<IntArray>.get(coord: Coord): Int {
    return this[coord.y][coord.x]
}

fun Array<BooleanArray>.getOrNull(coord: Coord): Boolean? {
    val row = getOrNull(coord.y)
    return row?.getOrNull(coord.x)
}

operator fun Array<BooleanArray>.set(coord: Coord, value: Boolean) {
    this[coord.y][coord.x] = value
}

operator fun Array<LongArray>.set(coord: Coord, value: Long) {
    this[coord.y][coord.x] = value
}

operator fun Array<IntArray>.set(coord: Coord, value: Int) {
    this[coord.y][coord.x] = value
}

fun <T> List<List<T>>.initializeVisitedGrid(): Array<BooleanArray> {
    val columnCount = first().size
    return Array(size) { BooleanArray(columnCount) }
}

fun Array<BooleanArray>.resetVisited() {
    for (arr in this) {
        for (c in arr.indices) {
            arr[c] = false
        }
    }
}