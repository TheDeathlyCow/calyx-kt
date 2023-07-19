package com.github.thedeathlycow.calyx.kt.syntax

import com.github.thedeathlycow.calyx.kt.Expansion
import com.github.thedeathlycow.calyx.kt.Options
import com.github.thedeathlycow.calyx.kt.Registry
import com.github.thedeathlycow.calyx.kt.production.Production

class TemplateNode(
    private val concatNodes: List<Production>,
    private val registry: Registry
) : Production {

    companion object {
        private const val EXPRESSION: String = "\\{[A-Za-z\\d_@$<>.]+\\}"
        private val EXPRESSION_REGEX: Regex = "((?<=$EXPRESSION\\s)|(?=$EXPRESSION\\s))".toRegex()
        private const val START_TOKEN: String = "{"
        private const val END_TOKEN: String = "}"
        private val DEREF_TOKEN: Regex = Regex(".")

        fun parse(raw: String, registry: Registry): TemplateNode {
            val fragments: List<String> = raw.split(EXPRESSION_REGEX)

            val concatNodes = mutableListOf<Production>()

            for (atom in fragments) {
                if (atom.isBlank()) continue

                // check if this is a template expression or atom
                if (atom.startsWith(START_TOKEN) && atom.endsWith(END_TOKEN)) {
                    // remove delimiters
                    val expression = atom.substring(1..(atom.length - 2))
                    // dereference the expression as an array of filter components
                    val components = expression.split(DEREF_TOKEN)

                    // check if we have a post-processing chain
                    if (components.size > 1) {
                        // generate a chained expression headed by a non terminal
                        concatNodes.add(ExpressionChain(components, registry))
                    } else {
                        // generate a standalone non terminal expression
                        concatNodes.add(ExpressionNode.parse(components[0], registry))
                    }
                } else {
                    // collect a string terminal
                    concatNodes.add(AtomNode(atom))
                }
            }

            return TemplateNode(concatNodes, registry)
        }

    }

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