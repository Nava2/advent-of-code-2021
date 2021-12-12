package net.navatwo.adventofcode2021

import net.navatwo.adventofcode2021.framework.Solution

private val loadedLines: MutableMap<String, List<String>> = mutableMapOf()

fun loadLines(resourceName: String): List<String> {
    return loadedLines.computeIfAbsent(resourceName) {
        ClassLoader.getSystemClassLoader().getResourceAsStream(resourceName)!!.use { resourceStream ->
            resourceStream.bufferedReader().use { reader ->
                reader.lineSequence().toList()
            }
        }
    }
}

fun <I> Solution<I>.parseResource(resourceName: String): I {
    return parse(loadLines(resourceName))
}