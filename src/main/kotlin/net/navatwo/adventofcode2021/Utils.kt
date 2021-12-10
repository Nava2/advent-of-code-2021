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