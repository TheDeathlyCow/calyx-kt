package com.github.thedeathlycow.calyx.kt

import com.github.thedeathlycow.calyx.kt.Expansion
import com.github.thedeathlycow.calyx.kt.Options
import com.github.thedeathlycow.calyx.kt.Registry
import org.junit.jupiter.api.RepeatedTest
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import kotlin.random.Random
import kotlin.test.assertEquals

class RegistryTest {

    @Test
    fun evaluateStartRule() {
        val registry = Registry()
        registry.defineRule("start", listOf("atom"))

        val exp = registry.evaluate("start")

        assertEquals(Expansion.Symbol.RESULT, exp.symbol)
        assertEquals("atom", exp.flattenToString())
    }

    @Test
    fun evaluateRecursiveRules() {
        val registry = Registry()
        registry.defineRule("start", listOf("{atom}"))
        registry.defineRule("atom", listOf("atom"))

        val exp = registry.evaluate("start")

        assertEquals(Expansion.Symbol.RESULT, exp.symbol)
        assertEquals("atom", exp.flattenToString())
    }

    @Test
    fun evaluateRulesWithInitializedContext() {
        val registry = Registry()
        registry.defineRule("start", listOf("{atom}"))

        val context = mapOf(
            "atom" to listOf("atom")
        )
        val exp = registry.evaluate("start", context)

        assertEquals(Expansion.Symbol.RESULT, exp.symbol)
        assertEquals("atom", exp.flattenToString())
    }

    @Test
    fun evaluateOnlyInitializedContext() {
        val registry = Registry()

        val context = mapOf(
            "start" to listOf("{atom}"),
            "atom" to listOf("atom")
        )

        val exp = registry.evaluate("start", context)

        assertEquals(Expansion.Symbol.RESULT, exp.symbol)
        assertEquals("atom", exp.flattenToString())
    }

    @Test
    fun memoizedRulesReturnIdenticalExpansion() {
        val registry = Registry(Options(seed = 1234))
        registry.defineRule("start", listOf("{@atom}{@atom}{@atom}"))
        registry.defineRule("atom", listOf("~", ":", ";"))

        val exp = registry.evaluate("start")

        assertEquals(Expansion.Symbol.RESULT, exp.symbol)
        assertEquals("~~~", exp.flattenToString())
    }

    @Test
    fun uniqueRylesReturnCycleSequence() {
        val registry = Registry(Options(223344))
        registry.defineRule("start", listOf("{\$medal};{\$medal};{\$medal}"))
        registry.defineRule("medal", listOf("gold", "silver", "bronze"))

        val exp = registry.evaluate("start")

        assertEquals(Expansion.Symbol.RESULT, exp.symbol)
        assertEquals("gold;bronze;silver", exp.flattenToString())
    }

}