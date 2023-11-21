package com.github.thedeathlycow.calyx.kt.syntax

import com.github.thedeathlycow.calyx.kt.Expansion
import com.github.thedeathlycow.calyx.kt.Options
import com.github.thedeathlycow.calyx.kt.Registry
import com.github.thedeathlycow.calyx.kt.production.Production

class ExpressionChain(
    private val ruleName: String,
    private val components: List<String>,
    private val registry: Registry
) : Production {

    companion object {
        fun parse(components: List<String>, registry: Registry): ExpressionChain {
            var ruleName = components[0]
            when (ruleName[0]) {
                ExpressionNode.MEMO_SIGIL, ExpressionNode.UNIQUE_SIGIL -> {
                    ruleName = ruleName.substring(1)
                }
            }

            return ExpressionChain(ruleName, components.drop(1), registry)
        }
    }

    override fun evaluate(options: Options): Expansion {
        val eval: Expansion = this.registry.expand(ruleName).evaluate(options)
        val initial: String = Expansion(Expansion.Symbol.EXPRESSION, eval.tail)
            .flatten()
            .toString()

        val modified: String = this.components
            .fold(initial) { accumulator, filterName ->
                registry.getFilterComponent(filterName)(accumulator, options)
            }

        return Expansion(Expansion.Symbol.EXPRESSION, Expansion(Expansion.Symbol.ATOM, modified))

    }

}