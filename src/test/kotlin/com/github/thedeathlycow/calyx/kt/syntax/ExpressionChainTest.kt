package com.github.thedeathlycow.calyx.kt.syntax

import com.github.thedeathlycow.calyx.kt.Expansion
import com.github.thedeathlycow.calyx.kt.Registry
import com.github.thedeathlycow.calyx.kt.UndefinedFilter
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

class ExpressionChainTest {

    @Test
    fun filterIsAppliedByDotNotation() {
        val registry = Registry()
        registry.defineRule("start", listOf("{greekLetter.uppercase}"))
        registry.defineRule("greekLetter", listOf("alpha"))

        val exp = registry.evaluate("start")

        assertEquals(Expansion.Symbol.RESULT, exp.symbol)
        assertEquals("ALPHA", exp.flattenToString())
    }

    @Test
    fun filtersCanBeChained() {
        val registry = Registry()
        registry.defineRule("start", listOf("{greekLetter.uppercase.emphasis}"))
        registry.defineRule("greekLetter", listOf("alpha"))

        val exp = registry.evaluate("start")
        assertEquals(Expansion.Symbol.RESULT, exp.symbol)
        assertEquals("*ALPHA*", exp.flattenToString())
    }

    @Test
    fun filtersAreEvaluatedLeftToRight() {
        val registry = Registry()
        registry.defineRule("start", listOf("{greekLetter.uppercase.length}"))
        registry.defineRule("greekLetter", listOf("alpha"))

        val exp = registry.evaluate("start")
        assertEquals(Expansion.Symbol.RESULT, exp.symbol)
        assertEquals("5", exp.flattenToString())
    }

    @Test
    fun undefinedFilterThrowsException() {
        val registry = Registry()
        registry.defineRule("start", listOf("{greekLetter.notAValidFilterName}"))
        registry.defineRule("greekLetter", listOf("alpha"))

        assertThrows<UndefinedFilter> {
            registry.evaluate("start")
        }
    }


}