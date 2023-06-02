package com.github.thedeathlycow.calyx.kt.production

import com.github.thedeathlycow.calyx.kt.Expansion
import com.github.thedeathlycow.calyx.kt.Options
import com.github.thedeathlycow.calyx.kt.Registry


class UniformBranch(
    private val choices: Array<Production>,
    private val registry: Registry
): ProductionBranch {

    override fun evaluateAt(index: Int, options: Options): Expansion {
        val tail: Expansion = choices[index].evaluate(options)
        return Expansion(Expansion.Symbol.UNIFORM_BRANCH, tail)
    }

    override fun evaluate(options: Options): Expansion {
        val index = options.random.nextInt(choices.size)
        return evaluateAt(index, options)
    }

    override val length: Int
        get() = choices.size
}