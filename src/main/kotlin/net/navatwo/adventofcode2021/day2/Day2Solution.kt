package net.navatwo.adventofcode2021.day2

import net.navatwo.adventofcode2021.framework.Solution

sealed class Day2Solution : Solution<Day2Solution.Action, Int> {
    object Part1 : Day2Solution() {
        override fun solve(inputs: List<Action>): Int {
            val position = inputs.fold(Position.ZERO) { position, action ->
                val (x, y, _) = position
                when (action) {
                    is Action.Forward -> position.copy(x = x + action.scalar)
                    is Action.Up -> position.copy(y = y - action.scalar)
                    is Action.Down -> position.copy(y = y + action.scalar)
                }
            }

            return position.result()
        }
    }

    object Part2 : Day2Solution() {
        override fun solve(inputs: List<Action>): Int {
            val position = inputs.fold(Position.ZERO) { position, action ->
                val (x, y, aim) = position
                when (action) {
                    is Action.Forward -> position.copy(
                        x = x + action.scalar,
                        y = y + aim * action.scalar,
                    )
                    is Action.Up -> position.copy(aim = aim - action.scalar)
                    is Action.Down -> position.copy(aim = aim + action.scalar)
                }
            }

            return position.result()
        }
    }

    override fun parse(lines: List<String>): List<Action> {
        val actionName = StringBuilder("forward".length)
        val actionLength = StringBuilder(10)

        return lines.map { line ->
            actionName.clear()
            actionLength.clear()

            var currentBuilder = actionName
            for (c in line) {
                if (c == ' ') {
                    currentBuilder = actionLength
                } else {
                    currentBuilder.append(c)
                }
            }

            val scalar = actionLength.toString().toInt()
            when (actionName.toString()) {
                "forward" -> Action.Forward(scalar)
                "up" -> Action.Up(scalar)
                "down" -> Action.Down(scalar)
                else -> error("Invalid action: $actionName")
            }
        }
    }

    data class Position(
        val x: Int,
        val y: Int,
        val aim: Int,
    ) {
        fun result() = x * y

        companion object {
            val ZERO = Position(0, 0, 0)
        }
    }


    sealed interface Action {
        val scalar: Int

        data class Forward(
            override val scalar: Int
        ) : Action

        data class Up(
            override val scalar: Int
        ) : Action

        data class Down(
            override val scalar: Int
        ) : Action
    }
}