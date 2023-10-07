package com.github.calyx.kt.production

import com.github.thedeathlycow.calyx.kt.Expansion
import com.github.thedeathlycow.calyx.kt.Options
import com.github.thedeathlycow.calyx.kt.Registry
import com.github.thedeathlycow.calyx.kt.production.UniformBranch
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class UniformBranchTest {

    @Test
    fun branchWithSingleChoiceTest() {
        val branch = UniformBranch.parse(listOf("atom"), Registry())

        val exp = branch.evaluate(Options())

        assertEquals(Expansion.Symbol.UNIFORM_BRANCH, exp.symbol)
        assertEquals(Expansion.Symbol.TEMPLATE, exp.tail[0].symbol)
    }

    @Test
    fun branchWithMultiChoiceTest() {
        val branch = UniformBranch.parse(listOf("lithium", "silicon", "carbon"), Registry())

        val exp = branch.evaluate(Options(seed = 1234))

        assertEquals(Expansion.Symbol.UNIFORM_BRANCH, exp.symbol)
        assertEquals(Expansion.Symbol.TEMPLATE, exp.tail[0].symbol)
        assertEquals(Expansion.Symbol.ATOM, exp.tail[0].tail[0].symbol)
        assertEquals("silicon", exp.tail[0].tail[0].term)
    }

}