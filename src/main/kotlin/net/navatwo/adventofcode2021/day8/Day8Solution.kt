package net.navatwo.adventofcode2021.day8

import net.navatwo.adventofcode2021.day8.Signal.A
import net.navatwo.adventofcode2021.day8.Signal.B
import net.navatwo.adventofcode2021.day8.Signal.C
import net.navatwo.adventofcode2021.day8.Signal.Companion.ALL_SIGNALS
import net.navatwo.adventofcode2021.day8.Signal.D
import net.navatwo.adventofcode2021.day8.Signal.E
import net.navatwo.adventofcode2021.day8.Signal.F
import net.navatwo.adventofcode2021.day8.Signal.G
import net.navatwo.adventofcode2021.framework.ComputedResult
import net.navatwo.adventofcode2021.framework.Solution

sealed class Day8Solution : Solution<Day8Solution.Input> {
    object Part1 : Day8Solution() {
        override fun solve(input: Input): ComputedResult {
            val uniqueDigits = setOf(
                Digit.One, Digit.Four, Digit.Seven, Digit.Eight,
            )

            val digitsBySegments = uniqueDigits.associateBy { it.segments }

            val requiredSignalCounts = uniqueDigits.mapTo(mutableSetOf()) { it.segmentCount }

            val digitEntries = input.entries.map { entry ->
                var knownLogic = KnownLogic(
                    requiredSignals = ALL_SIGNALS,
                    knownSignalToSegment = mapOf(),
                    remainingDigits = Digit.values().toSet(),
                    signalMappings = Signal.values().associateWith { KnownLogic.Options(ALL_SIGNALS) },
                )

                val patterns = entry.signals.filter { it.signals.size in requiredSignalCounts }
                knownLogic = Rule.DigitUniqueCountRule.apply(knownLogic, patterns).simplify()

                while (!knownLogic.mapped()) {
                    knownLogic = Rule.DistributeSignalsToSegments.apply(knownLogic, patterns).simplify()
                }

                entry.outputs.asSequence()
                    .map { pattern ->
                        pattern.signals.mapTo(mutableSetOf()) { s -> knownLogic.knownSignalToSegment.getValue(s) }
                    }
                    .mapNotNull { segments -> digitsBySegments[segments] }
                    .toList()
            }

            return ComputedResult.Simple(
                digitEntries.asSequence()
                    .flatMap { digits -> digits.asSequence().filter { it in uniqueDigits } }
                    .count()
            )
        }
    }

    override fun parse(lines: List<String>): Input {
        return Input(
            entries = lines.map { line ->
                val (signals, outputs) = line.split('|').map { parsePatterns(it) }
                Entry(
                    signals = signals,
                    outputs = outputs,
                )
            }
        )
    }

    data class Input(
        val entries: List<Entry>,
    )
}

data class KnownLogic(
    val requiredSignals: Set<Signal>,
    val knownSignalToSegment: Map<Signal, Signal>,
    val remainingDigits: Set<Digit>,
    val signalMappings: Map<Signal, Options>,
) {
    fun mapped(): Boolean = requiredSignals == knownSignalToSegment.keys

    fun simplify(): KnownLogic {
        lateinit var current: KnownLogic
        var next = this
        do {
            current = next
            next = simplifyHelper(current)
        } while (current != next)

        return next
    }

    companion object {
        private fun simplifyHelper(that: KnownLogic): KnownLogic {
            val knownSignalToSegment = that.knownSignalToSegment.toMutableMap()
            var knownSegments: Set<Signal>
            var signalMappings = that.signalMappings.filterKeys { k -> k !in knownSignalToSegment.keys }

            do {
                knownSegments = knownSignalToSegment.values.toSet()
                signalMappings = signalMappings.entries.asSequence()
                    .filter { (k, _) -> k !in knownSignalToSegment.keys }
                    .associate { (k, options) ->
                        k to options.copy(potentials = options.potentials - knownSegments)
                    }

                var changeFound = false
                for ((signal, options) in signalMappings) {
                    val knownSegment = options.knownSegment()
                    if (knownSegment != null) {
                        // signal -> knownSegment
                        changeFound = true
                        knownSignalToSegment[signal] = knownSegment
                    }
                }
            } while (changeFound)

            knownSegments = knownSignalToSegment.values.toSet()
            signalMappings = signalMappings.mapValues { (_, options) ->
                options.copy(potentials = options.potentials - knownSegments)
            }

            val remainingDigits = that.remainingDigits.filterTo(mutableSetOf()) { d ->
                (d.segments - knownSegments).isEmpty()
            }

            return that.copy(
                knownSignalToSegment = knownSignalToSegment,
                remainingDigits = remainingDigits,
                signalMappings = that.signalMappings + signalMappings,
            )
        }
    }

    data class Options(
        val potentials: Set<Signal>,
    ) {
        fun knownSegment(): Signal? {
            // if more than one entry, too many remaining
            return potentials.singleOrNull()
        }

        companion object {
            val REQUIRED_IMPOSSIBLE_COUNT = ALL_SIGNALS.size - 1
        }
    }
}

val RULES = listOf(
    Rule.DigitUniqueCountRule,
    Rule.DistributeSignalsToSegments,
)

sealed interface Rule {
    fun apply(knownLogic: KnownLogic, patterns: Collection<Pattern>): KnownLogic

    object DigitUniqueCountRule : Rule {
        override fun apply(knownLogic: KnownLogic, patterns: Collection<Pattern>): KnownLogic {
            return patterns.fold(knownLogic) { acc, pattern -> applyFromPattern(acc, pattern) }
        }

        private fun applyFromPattern(knownLogic: KnownLogic, pattern: Pattern): KnownLogic {
            val digitToConsider = Digit.DIGITS_BY_SEGMENT_COUNT.getValue(pattern.signals.size).singleOrNull()
            if (digitToConsider == null || digitToConsider == Digit.Eight) return knownLogic

            val possibleSignals = digitToConsider.segments - knownLogic.knownSignalToSegment.keys
            val signalMappings = knownLogic.signalMappings.toMutableMap()

            // Each signal in the pattern is _one of_ digit.segments
            var changeFound = false
            for (signal in pattern.signals) {
                signalMappings.compute(signal) { _, currentOptions ->
                    checkNotNull(currentOptions)
                    val potentials = currentOptions.potentials intersect possibleSignals
                    if (potentials.size == currentOptions.potentials.size) {
                        currentOptions
                    } else {
                        changeFound = true
                        currentOptions.copy(
                            potentials = potentials,
                        )
                    }
                }
            }

            if (!changeFound) return knownLogic

            return knownLogic.copy(
                remainingDigits = knownLogic.remainingDigits - digitToConsider,
                signalMappings = signalMappings,
            )
        }
    }

    object DistributeSignalsToSegments : Rule {
        override fun apply(knownLogic: KnownLogic, patterns: Collection<Pattern>): KnownLogic {
            val signalMappings = knownLogic.signalMappings.toMutableMap()
            val knownSignalToSegment = knownLogic.knownSignalToSegment.toMutableMap()

            if (!distributeSafeMultiOptions(signalMappings, knownSignalToSegment)) return knownLogic

            return knownLogic.copy(
                knownSignalToSegment = knownSignalToSegment,
                signalMappings = knownLogic.signalMappings + signalMappings,
            )
        }

        private fun distributeSafeMultiOptions(
            simplifiedOptions: MutableMap<Signal, KnownLogic.Options>,
            knownSignalToSegment: MutableMap<Signal, Signal>
        ): Boolean {
            val groupSegmentsBySameSignals = simplifiedOptions.entries.asSequence()
                .filter { it.value.knownSegment() == null }
                .groupBy({ it.value.potentials }) { it.key }

            var changeFound = false
            for (potentialSignals in groupSegmentsBySameSignals.keys.sortedBy { it.size }) {
                val segmentsWithSameSignal = groupSegmentsBySameSignals.getValue(potentialSignals)

                // We know that we can assign one to each one signal safely
                if (potentialSignals.size == segmentsWithSameSignal.size) {
                    changeFound = true

                    for ((signal, segment) in segmentsWithSameSignal.zip(potentialSignals)) {
                        knownSignalToSegment[signal] = segment

                        simplifiedOptions.compute(signal) { _, currentOptions ->
                            checkNotNull(currentOptions)
                            currentOptions.copy(
                                potentials = setOf(segment),
                            )
                        }
                    }
                }
            }

            return changeFound
        }
    }
}

enum class Signal {
    A, B, C, D, E, F, G;

    companion object {
        val ALL_SIGNALS = values().toSet()

        fun inverse(signals: Set<Signal>): Set<Signal> {
            return ALL_SIGNALS - signals
        }

        fun valueOf(c: Char) = when (c) {
            'a', 'A' -> A
            'b', 'B' -> B
            'c', 'C' -> C
            'd', 'D' -> D
            'e', 'E' -> E
            'f', 'F' -> F
            'g', 'G' -> G
            else -> throw IllegalArgumentException("Invalid input")
        }
    }
}

enum class Digit(
    val segments: Set<Signal>,
) {
    Zero(segments = setOf(A, B, C, E, F, G)),
    One(segments = setOf(C, F)),
    Two(segments = setOf(A, C, D, E, G)),
    Three(segments = setOf(A, C, D, F, G)),
    Four(segments = setOf(B, C, D, F)),
    Five(segments = setOf(A, B, D, F, G)),
    Six(segments = setOf(A, B, D, E, F, G)),
    Seven(segments = setOf(A, C, F)),
    Eight(segments = setOf(A, B, C, D, E, F, G)),
    Nine(segments = setOf(A, B, C, D, F, G)),

    ;

    val segmentCount = segments.size
    val inverseSegments = Signal.inverse(segments)

    companion object {
        val DIGITS_BY_SEGMENT_COUNT = values().groupBy { it.segmentCount }.mapValues { it.value.toSet() }
    }
}

private fun parsePatterns(input: String): List<Pattern> {
    return input.splitToSequence(' ')
        .filter { it.isNotBlank() }
        .map { p ->
            Pattern(
                signals = p.trim().mapTo(mutableSetOf()) { Signal.valueOf(it) }
            )
        }
        .toList()
}

data class Entry(
    val signals: List<Pattern>,
    val outputs: List<Pattern>,
)

data class Pattern(
    val signals: Set<Signal>,
)