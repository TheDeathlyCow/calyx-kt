package com.github.thedeathlycow.calyx.kt.syntax

import com.github.thedeathlycow.calyx.kt.Expansion
import com.github.thedeathlycow.calyx.kt.Options
import com.github.thedeathlycow.calyx.kt.Registry
import com.github.thedeathlycow.calyx.kt.production.Production

class MemoNode(
    private val symbol: String,
    private val registry: Registry
): Production {



    override fun evaluate(options: Options): Expansion {
        val eval = registry.memoizeExpansion(symbol)
        return Expansion(Expansion.Symbol.MEMO, eval)
    }
}