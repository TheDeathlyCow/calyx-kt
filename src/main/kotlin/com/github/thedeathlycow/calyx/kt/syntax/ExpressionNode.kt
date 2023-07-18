package com.github.thedeathlycow.calyx.kt.syntax

import com.github.thedeathlycow.calyx.kt.Expansion
import com.github.thedeathlycow.calyx.kt.Options
import com.github.thedeathlycow.calyx.kt.Registry
import com.github.thedeathlycow.calyx.kt.production.Production

class ExpressionNode(
    private val reference: String,
    private val registry: Registry
): Production {

    companion object {

    }

    override fun evaluate(options: Options): Expansion {
        val eval = registry.expand(reference).evaluate(options)
        return Expansion(Expansion.Symbol.EXPRESSION, eval.tail)
    }
}