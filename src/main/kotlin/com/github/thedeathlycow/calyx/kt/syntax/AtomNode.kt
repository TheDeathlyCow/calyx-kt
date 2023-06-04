package com.github.thedeathlycow.calyx.kt.syntax

import com.github.thedeathlycow.calyx.kt.Expansion
import com.github.thedeathlycow.calyx.kt.Options
import com.github.thedeathlycow.calyx.kt.production.Production

class AtomNode(
    private val atom: String
) : Production {

    override fun evaluate(options: Options): Expansion {
        return Expansion(Expansion.Symbol.ATOM, atom)
    }
    
}