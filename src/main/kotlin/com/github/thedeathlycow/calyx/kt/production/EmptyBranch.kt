package com.github.thedeathlycow.calyx.kt.production

import com.github.thedeathlycow.calyx.kt.Expansion
import com.github.thedeathlycow.calyx.kt.Options

class EmptyBranch: ProductionBranch {

    override fun evaluateAt(index: Int, options: Options): Expansion {
        return Expansion(Expansion.Symbol.EMPTY_BRANCH, Expansion(Expansion.Symbol.ATOM, ""))
    }

    override fun evaluate(options: Options): Expansion {
        return Expansion(Expansion.Symbol.EMPTY_BRANCH, Expansion(Expansion.Symbol.ATOM, ""))
    }

    override val length: Int
        get() = 1

}