package com.github.thedeathlycow.calyx.kt.production

import com.github.thedeathlycow.calyx.kt.Expansion
import com.github.thedeathlycow.calyx.kt.Options

interface ProductionBranch: Production {

    fun evaluateAt(index: Int, options: Options): Expansion

    val size: Int

}