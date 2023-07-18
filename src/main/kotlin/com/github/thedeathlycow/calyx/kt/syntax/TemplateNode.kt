package com.github.thedeathlycow.calyx.kt.syntax

import com.github.thedeathlycow.calyx.kt.Expansion
import com.github.thedeathlycow.calyx.kt.Options
import com.github.thedeathlycow.calyx.kt.Registry
import com.github.thedeathlycow.calyx.kt.production.Production

class TemplateNode(
    private val concatNodes: List<Production>,
    private val registry: Registry
) : Production {

    override fun evaluate(options: Options): Expansion {
        return Expansion(
            Expansion.Symbol.TEMPLATE,
            concatNodes.asSequence()
                .map {
                    it.evaluate(options)
                }
                .toList()
        )
    }
}