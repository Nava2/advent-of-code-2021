package net.navatwo.adventofcode2021.day8

import net.navatwo.adventofcode2021.day8.Signal.A
import net.navatwo.adventofcode2021.day8.Signal.B
import net.navatwo.adventofcode2021.day8.Signal.C
import net.navatwo.adventofcode2021.day8.Signal.D
import net.navatwo.adventofcode2021.day8.Signal.E
import net.navatwo.adventofcode2021.day8.Signal.F
import net.navatwo.adventofcode2021.day8.Signal.G
import net.navatwo.adventofcode2021.framework.ComputedResult
import net.navatwo.adventofcode2021.framework.Solution

sealed class Day8Solution : Solution<Day8Solution.Input> {
    object Part1 : Day8Solution() {
        override fun solve(input: Input): ComputedResult {
            val digitEntries = solveDigits(Digit.ALL_DIGITS, input)

            return ComputedResult.Simple(
                digitEntries.fold(0L) { acc, digits -> acc + digits.count { it.isUniqueSegments() } }
            )
        }
    }

    object Part2 : Day8Solution() {
        override fun solve(input: Input): ComputedResult {
            val digitEntries = solveDigits(Digit.ALL_DIGITS, input)

            return ComputedResult.Simple(
                digitEntries.sumOf { digits -> digits.fold(0L) { acc, d -> acc * 10 + d.value } }
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

    protected fun solveDigits(
        digitsToSolveFor: Set<Digit>,
        input: Input
    ): List<List<Digit>> {
        val digitsBySegments = digitsToSolveFor.associateBy { it.segments }

        return input.entries.map { entry ->
            val result = KnownLogic.DEFAULT
                .applyRule(Rule.DigitFrequencyRule, entry.signals)
                .applyRule(Rule.DigitUniqueCountRule, entry.signals)
                .simplify()

            check(result.mappedSignalToSegment.size == Signal.values().size)

            entry.outputs.asSequence()
                .map { pattern ->
                    pattern.signals.mapTo(mutableSetOf()) { s -> result.mappedSignalToSegment.getValue(s) }
                }
                .mapNotNull { segments -> digitsBySegments[segments] }
                .toList()
        }
    }
}

data class KnownLogic(
    val signalMappings: Map<Signal, Options>,
) {
    val mappedSignalToSegment: Map<Signal, Signal> = signalMappings.entries
        .mapNotNull { (signal, options) -> options.knownSegment()?.let { signal to it } }
        .toMap()

    val mappedSignals: Set<Signal>
        get() = mappedSignalToSegment.keys

    fun applyRule(rule: Rule, patterns: Collection<Pattern>): KnownLogic {
        return rule.apply(this, patterns)
    }

    fun simplify(): KnownLogic {
        lateinit var current: KnownLogic
        var next = this
        do {
            current = next
            next = simplifyHelper(current)
        } while (current != next)

        return next
    }

    fun setOptionsToKnown(knownSignalToSegment: Map<Signal, Signal>): KnownLogic {
        if (knownSignalToSegment.isEmpty()) return this

        val signalMappings = signalMappings.mapValues { (signal, options) ->
            knownSignalToSegment[signal]?.let { segment -> Options.knownSegment(segment) }
                ?: options
        }

        return copy(signalMappings = signalMappings)
    }

    companion object {
        val DEFAULT: KnownLogic = KnownLogic(
            signalMappings = Signal.values().associateWith { Options.DEFAULT },
        )

        private fun simplifyHelper(that: KnownLogic): KnownLogic {
            val knownSignalToSegment = that.mappedSignalToSegment.toMutableMap()
            var knownSegments: Set<Signal>
            var signalMappings = that.signalMappings.filterKeys { k -> k !in knownSignalToSegment.keys }

            do {
                knownSegments = knownSignalToSegment.values.toSet()
                signalMappings = signalMappings.entries.asSequence()
                    .filter { (_, o) -> o.potentialSegments.size > 1 }
                    .associate { (k, options) ->
                        k to options.copy(potentialSegments = options.potentialSegments - knownSegments)
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
                options.copy(potentialSegments = options.potentialSegments - knownSegments)
            }

            return that.copy(signalMappings = that.signalMappings + signalMappings)
                .setOptionsToKnown(knownSignalToSegment)
        }
    }

    data class Options(
        val potentialSegments: Set<Signal>,
    ) {
        fun knownSegment(): Signal? {
            // if more than one entry, too many remaining
            return potentialSegments.singleOrNull()
        }

        companion object {
            val DEFAULT = Options(Signal.ALL_SIGNALS)
            fun knownSegment(segment: Signal) = Options(potentialSegments = setOf(segment))
        }
    }
}

sealed interface Rule {
    fun apply(knownLogic: KnownLogic, patterns: Collection<Pattern>): KnownLogic

    object DigitUniqueCountRule : Rule {
        override fun apply(knownLogic: KnownLogic, patterns: Collection<Pattern>): KnownLogic {
            return patterns.fold(knownLogic) { acc, pattern -> applyFromPattern(acc, pattern) }
        }

        private fun applyFromPattern(knownLogic: KnownLogic, pattern: Pattern): KnownLogic {
            val digitToConsider = Digit.DIGITS_BY_SEGMENT_COUNT.getValue(pattern.signals.size).singleOrNull()
                ?: return knownLogic
            if (digitToConsider == Digit.Eight || !digitToConsider.isUniqueSegments()) return knownLogic

            val possibleSignals = digitToConsider.segments
            if ((possibleSignals - knownLogic.mappedSignals).isEmpty()) return knownLogic

            val signalMappings = knownLogic.signalMappings.toMutableMap()

            // Each signal in the pattern is _one of_ digit.segments
            var changeFound = false
            for (signal in pattern.signals) {
                signalMappings.computeIfPresent(signal) { _, currentOptions ->
                    val potentials = currentOptions.potentialSegments intersect possibleSignals
                    if (currentOptions.knownSegment() != null ||
                        potentials.size == currentOptions.potentialSegments.size
                    ) {
                        currentOptions
                    } else {
                        changeFound = true
                        currentOptions.copy(potentialSegments = potentials)
                    }
                }
            }

            if (!changeFound) return knownLogic

            return knownLogic.copy(signalMappings = signalMappings)
        }
    }

    object DigitFrequencyRule : Rule {
        private val segmentFrequencies: Map<Int, Set<Signal>> = run {
            val frequencies = computeFrequencies(Digit.values().map { it.segments })

            val signalsByFrequency = frequencies.entries.groupBy({ it.value }) { it.key }

            signalsByFrequency.mapValues { it.value.toSet() }
        }

        override fun apply(knownLogic: KnownLogic, patterns: Collection<Pattern>): KnownLogic {
            val signalMappings = knownLogic.signalMappings.toMutableMap()

            for ((signal, frequency) in computeFrequencies(patterns.map { it.signals })) {
                val segmentsWithFrequency = segmentFrequencies.getOrElse(frequency) { setOf() }
                signalMappings.computeIfPresent(signal) { _, options ->
                    if (segmentsWithFrequency.size == 1) {
                        KnownLogic.Options.knownSegment(segmentsWithFrequency.single())
                    } else {
                        options.copy(potentialSegments = options.potentialSegments intersect segmentsWithFrequency)
                    }
                }
            }

            return knownLogic.copy(signalMappings = signalMappings)
        }

        private fun computeFrequencies(patterns: Collection<Set<Signal>>): Map<Signal, Int> {
            return Signal.values().associateWith { signal ->
                patterns.count { signal in it }
            }
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
    val value: Long,
    val segments: Set<Signal>,
) {
    Zero(value = 0, segments = setOf(A, B, C, E, F, G)),
    One(value = 1, segments = setOf(C, F)),
    Two(value = 2, segments = setOf(A, C, D, E, G)),
    Three(value = 3, segments = setOf(A, C, D, F, G)),
    Four(value = 4, segments = setOf(B, C, D, F)),
    Five(value = 5, segments = setOf(A, B, D, F, G)),
    Six(value = 6, segments = setOf(A, B, D, E, F, G)),
    Seven(value = 7, segments = setOf(A, C, F)),
    Eight(value = 8, segments = setOf(A, B, C, D, E, F, G)),
    Nine(value = 9, segments = setOf(A, B, C, D, F, G)),

    ;

    val segmentCount = segments.size

    fun isUniqueSegments(): Boolean = when (this) {
        One, Four, Seven, Eight -> true
        else -> false
    }

    companion object {
        val DIGITS_BY_SEGMENT_COUNT = values().groupBy { it.segmentCount }.mapValues { it.value.toSet() }
        val ALL_DIGITS = values().toSet()
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