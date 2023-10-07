package com.github.calyx.kt.grammar

import com.github.thedeathlycow.calyx.kt.Grammar
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class WeightedBranchTest {

    @Test
    fun declareIntWeightedBranchWithRuleBuilder() {
        val grammar = Grammar(123456)
        grammar.start("{ratio}")

        val weights = HashMap<String, Int>()
        weights["4/7"] = 4
        weights["2/7"] = 2
        weights["1/7"] = 1
        grammar.rule("ratio", weights)

        val result = grammar.generate()

        assertEquals("4/7", result.text)
    }

}