package com.github.thedeathlycow.calyx.kt

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import kotlin.test.assertEquals

class GrammarJsonTest {

    @Test
    fun parseStartRule() {
        val grammar: Grammar = Grammar.loadJson(
            """
                {
                    "start": "Hello world!"
                }
                """.trimIndent()
        )

        assertEquals("Hello world!", grammar.generate().text)
    }

    @Test
    fun parseNoStartRuleThrows() {
        assertThrows<GrammarParseException> {
            Grammar.loadJson(
                """
                    {
                        "not_start": "Hello world!"
                    }
                """.trimIndent()
            )
        }
    }

    @Test
    fun parseSimpleRecursiveRule() {
        val grammar: Grammar = Grammar.loadJson(
            """
                {
                    "start": "Hello {world}!",
                    "world": "earth"
                }
                """.trimIndent()
        )

        assertEquals("Hello earth!", grammar.generate().text)
    }

    @Test
    fun parseUniformRule() {
        val grammar: Grammar = Grammar.loadJson(
            """
                {
                    "start": ["Hello world!"]
                }
                """.trimIndent()
        )

        assertEquals("Hello world!", grammar.generate().text)
    }

    @Test
    fun parseRecursiveUniformRule() {
        val grammar: Grammar = Grammar.loadJson(
            """
                {
                    "start": ["Hello {world}!"],
                    "world": "earth"
                }
                """.trimIndent()
        )

        assertEquals("Hello earth!", grammar.generate().text)
    }

    @Test
    fun parseWeightedRule() {
        val grammar: Grammar = Grammar.loadJson(
            """
                {
                    "start": {
                        "Hello world!": 1
                    }
                }
                """.trimIndent()
        )

        assertEquals("Hello world!", grammar.generate().text)
    }

    @Test
    fun parseRecursiveWeightedRule() {
        val grammar: Grammar = Grammar.loadJson(
            """
                {
                    "start": {
                        "Hello {world}!": 1
                    },
                    "world": "earth"
                }
                """.trimIndent()
        )

        assertEquals("Hello earth!", grammar.generate().text)
    }

    @ParameterizedTest
    @ValueSource(
        strings = [
            "NaN",
            "true",
            "[]",
            """{"hello": "lol"}"""
        ]
    )
    fun parseRecursiveWeightedRule_failsOnNonNumber(invalid: String) {
        assertThrows<GrammarParseException> {
            Grammar.loadJson(
                """
                {
                    "start": {
                        "Hello {world}!": $invalid
                    },
                    "world": "earth"
                }
                """.trimIndent()
            )
        }
    }


}