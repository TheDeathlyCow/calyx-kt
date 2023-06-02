package com.github.thedeathlycow.calyx.kt.production

import com.github.thedeathlycow.calyx.kt.Expansion
import com.github.thedeathlycow.calyx.kt.Options

class EmptyBranch: ProductionBranch {

    override fun evalulateAt(index: Int, options: Options): Expansion {
        TODO("Not yet implemented")
    }

    override fun evaluate(options: Options): Expansion {
        TODO("Not yet implemented")
    }

    override val length: Int
        get() = 1

}