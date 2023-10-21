package com.github.calyx.kt.test

import com.github.thedeathlycow.calyx.kt.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.Ignore
import kotlin.test.assertEquals

class FilterTest {

    @Test
    fun upperCase() {
        val registry = Registry()
        registry.defineRule("start", listOf("{city.uppercase}"))
        registry.defineRule("city", listOf("Whangārei"))

        val exp = registry.evaluate("start")

        assertEquals(Expansion.Symbol.RESULT, exp.symbol)
        assertEquals("WHANGĀREI", exp.flatten().toString())
    }

    @Test
    fun lowerCase() {
        val registry = Registry()
        registry.defineRule("start", listOf("{city.lowercase}"))
        registry.defineRule("city", listOf("Whangārei"))

        val exp = registry.evaluate("start")

        assertEquals(Expansion.Symbol.RESULT, exp.symbol)
        assertEquals("whangārei", exp.flatten().toString())
    }

    @Test
    @Ignore
    fun sentenceCase() {
        val registry = Registry()
        registry.defineRule("start", listOf("{city.sentencecase}"))
        registry.defineRule("sentence", listOf("Texas is in USA. london is in England."))

        val exp = registry.evaluate("start")

        assertEquals(Expansion.Symbol.RESULT, exp.symbol)
        assertEquals("Texas is in USA. London is in England.", exp.flatten().toString())
    }

    @Test
    @Ignore
    fun titleCase() {
        val registry = Registry()
        registry.defineRule("start", listOf("{city.titlecase}"))
        registry.defineRule("sentence", listOf("New York is in USA. London is in England."))

        val exp = registry.evaluate("start")

        assertEquals(Expansion.Symbol.RESULT, exp.symbol)
        assertEquals("New York is in USA. London is in England.", exp.flatten().toString())
    }

    @Test
    fun canDefineACustomFilter() {
        val registry = Registry()

        registry.addFilters(TestFilter)

        registry.defineRule("start", listOf("{anadrome.backwards}"))
        registry.defineRule("anadrome", listOf("desserts"))

        val exp = registry.evaluate("start")

        assertEquals(Expansion.Symbol.RESULT, exp.symbol)
        assertEquals("stressed", exp.flatten().toString())
    }

    @Test
    fun incorrectFilterSignatureThrows() {
        val registry = Registry()

        registry.addFilters(TestFilter)

        registry.defineRule("start", listOf("{ball.incorrectparams}"))
        registry.defineRule("ball", listOf("⚽", "🏀", "⚾"))

        assertThrows<IncorrectFilterSignature> {
            registry.evaluate("start")
        }
    }

    @Test
    fun incorrectFilterParamCountThrows() {
        val registry = Registry()

        registry.addFilters(TestFilter)

        registry.defineRule("start", listOf("{ball.incorrectparamcount}"))
        registry.defineRule("ball", listOf("⚽", "🏀", "⚾"))

        assertThrows<IncorrectFilterSignature> {
            registry.evaluate("start")
        }
    }

    @Suppress("unused")
    internal object TestFilter {

        @FilterName("backwards")
        fun backwards(input: String, options: Options): String {
            return input.reversed()
        }

        @FilterName("incorrectparams")
        fun incorrectParams(input: String, options: String): String = ""

        @FilterName("incorrectparamcount")
        fun incorrectParamCount(input: String): String = ""
    }
}