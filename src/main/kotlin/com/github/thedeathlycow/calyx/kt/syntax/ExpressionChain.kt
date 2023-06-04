package com.github.thedeathlycow.calyx.kt.syntax

import com.github.thedeathlycow.calyx.kt.Expansion
import com.github.thedeathlycow.calyx.kt.IncorrectFilterSignature
import com.github.thedeathlycow.calyx.kt.Options
import com.github.thedeathlycow.calyx.kt.Registry
import com.github.thedeathlycow.calyx.kt.production.Production

class ExpressionChain(
    private val components: Array<String>,
    private val registry: Registry
) : Production {

    override fun evaluate(options: Options): Expansion {
        val eval: Expansion = this.registry.expand(this.components[0]).evaluate(options)
        val initial: String = Expansion(Expansion.Symbol.EXPRESSION, eval.tail)
            .flatten()
            .toString()

        val modified: String = this.components.asSequence()
            .drop(1)
            .fold(initial) { accumulator, filterName ->
                try {
                    registry.getFilterComponent(filterName).invoke(accumulator)
                } catch (e: IllegalArgumentException) {
                    throw IncorrectFilterSignature(filterName, e)
                }
            }

        return Expansion(Expansion.Symbol.EXPRESSION, Expansion(Expansion.Symbol.ATOM, modified))

    }

}