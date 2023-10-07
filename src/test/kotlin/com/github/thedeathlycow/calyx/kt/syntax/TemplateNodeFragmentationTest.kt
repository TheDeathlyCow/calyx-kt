package com.github.calyx.kt.syntax

import com.github.thedeathlycow.calyx.kt.InvalidExpression
import com.github.thedeathlycow.calyx.kt.syntax.TemplateNode
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import kotlin.test.assertEquals

class TemplateNodeFragmentationTest {

    @Test
    fun fragmentedTokensIncludesExpressionsAndAtoms() {
        val fragments: List<String> = TemplateNode.fragmentString("{expression} atom {expression}")
        assertEquals(listOf("{expression}", " atom ", "{expression}"), fragments)
    }

    @Test
    fun testFragmentedTokensStartWithAtoms() {
        val fragments: List<String> = TemplateNode.fragmentString("start atom {expression} atom {expression}")
        assertEquals(listOf("start atom ", "{expression}", " atom ", "{expression}"), fragments)
    }

    @Test
    fun fragmentTokenToTwoSpaceSeparatedExpressions() {
        val fragments: List<String> = TemplateNode.fragmentString("{expression} {expression}")
        assertEquals(listOf("{expression}", " ", "{expression}"), fragments)
    }

    @Test
    fun fragmentTokenWithJustExpressions() {
        val fragments: List<String> = TemplateNode.fragmentString("{expression1}{expression2}")
        assertEquals(listOf("{expression1}", "{expression2}"), fragments)
    }

    @Test
    fun fragmentTokenWithJustAtoms() {
        val fragments: List<String> = TemplateNode.fragmentString("this is just a single atom")
        assertEquals(listOf("this is just a single atom"), fragments)
    }

    @Test
    fun fragmentDotChainTokens() {
        val fragments: List<String> = TemplateNode.fragmentString("{expression.filter}")
        assertEquals(listOf("{expression.filter}"), fragments)
    }

    @Test
    fun fragmentEmptyTokens() {
        val fragments: List<String> = TemplateNode.fragmentString("")
        assertEquals(listOf<String>(), fragments)
    }

    @ParameterizedTest
    @ValueSource(
        strings = [
            "{contains a space}",
            "{%% invalid chars}",
            "{}"
        ]
    )
    fun fragmentInvalidExpressionThrows(input: String) {
        assertThrows<InvalidExpression> {
            TemplateNode.fragmentString(input)
        }
    }
}