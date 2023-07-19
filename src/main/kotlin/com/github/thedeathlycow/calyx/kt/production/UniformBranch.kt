package com.github.thedeathlycow.calyx.kt.production

import com.github.thedeathlycow.calyx.kt.Expansion
import com.github.thedeathlycow.calyx.kt.Options
import com.github.thedeathlycow.calyx.kt.Registry
import com.github.thedeathlycow.calyx.kt.syntax.TemplateNode


class UniformBranch(
    private val choices: List<Production>,
    private val registry: Registry
): ProductionBranch {

    companion object {

        fun parse(raw: List<String>, registry: Registry): UniformBranch {

            val choices = raw.asSequence()
                .map {
                    TemplateNode.parse(it, registry)
                }
                .toList()

            return UniformBranch(choices, registry)
        }

    }

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