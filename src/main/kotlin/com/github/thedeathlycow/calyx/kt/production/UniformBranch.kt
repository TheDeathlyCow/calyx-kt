package com.github.thedeathlycow.calyx.kt.production

import com.github.thedeathlycow.calyx.kt.Expansion
import com.github.thedeathlycow.calyx.kt.Options
import com.github.thedeathlycow.calyx.kt.Registry

class UniformBranch(
    private val choices: Array<Production>,
    private val registry: Registry
): ProductionBranch {

    override fun evalulateAt(index: Int, options: Options): Expansion {
        TODO("Not yet implemented")
    }

    override fun evaluate(options: Options): Expansion {
        TODO("Not yet implemented")
    }

    override val length: Int
        get() = choices.size
}