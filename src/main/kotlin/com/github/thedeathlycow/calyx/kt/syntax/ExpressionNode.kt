package com.github.thedeathlycow.calyx.kt.syntax

import com.github.thedeathlycow.calyx.kt.Expansion
import com.github.thedeathlycow.calyx.kt.Options
import com.github.thedeathlycow.calyx.kt.Registry
import com.github.thedeathlycow.calyx.kt.production.Production

class ExpressionNode(
    private val reference: String,
    private val registry: Registry
) : Production {

    companion object {
        private const val MEMO_SIGIL: Char = '@'
        private const val UNIQUE_SIGIL: Char = '$'

        fun parse(raw: String, registry: Registry): Production {
            return when {
                raw[0] == MEMO_SIGIL -> {
                    MemoNode(raw.substring(0), registry)
                }

                raw[0] == UNIQUE_SIGIL -> {
                    UniqNode(raw.substring(0), registry)
                }

                else -> {
                    ExpressionNode(raw, registry)
                }
            }
        }
    }

    override fun evaluate(options: Options): Expansion {
        val eval = registry.expand(reference).evaluate(options)
        return Expansion(Expansion.Symbol.EXPRESSION, eval.tail)
    }
}