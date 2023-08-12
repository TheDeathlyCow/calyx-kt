package com.github.thedeathlycow.calyx.kt.syntax

import com.github.thedeathlycow.calyx.kt.Expansion
import com.github.thedeathlycow.calyx.kt.InvalidExpression
import com.github.thedeathlycow.calyx.kt.Options
import com.github.thedeathlycow.calyx.kt.Registry
import com.github.thedeathlycow.calyx.kt.production.Production

class TemplateNode(
    private val concatNodes: List<Production>,
    private val registry: Registry
) : Production {

    companion object {
        private val EXPRESSION_REGEX: Regex = "\\{[A-Za-z\\d_@$<>.]+}".toRegex()
        private const val START_TOKEN: Char = '{'
        private const val END_TOKEN: Char = '}'
        private const val DEREF_TOKEN: Char = '.'

        fun parse(raw: String, registry: Registry): TemplateNode {
            val fragments: List<String> = fragmentString(raw)

            val concatNodes = mutableListOf<Production>()

            for (atom in fragments) {
                if (atom.isEmpty()) continue

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

        fun fragmentString(raw: String): List<String> {
            val fragments = mutableListOf<String>()

            var lastExpressionPos = 0
            var expressionPos = raw.indexOf(START_TOKEN, 0)

            while (expressionPos != -1) {
                val expressionEndPos = raw.indexOf(END_TOKEN, expressionPos)

                if (expressionEndPos == -1) {
                    break // no closing bracket found, stop parsing
                }

                if (expressionPos > lastExpressionPos) {
                    fragments.add(raw.substring(lastExpressionPos until expressionPos))
                }

                // add the expression to the fragments
                val expression = raw.substring(expressionPos..expressionEndPos)

                if (!EXPRESSION_REGEX.matches(expression)) {
                    throw InvalidExpression(expression)
                }

                fragments.add(expression)

                lastExpressionPos = expressionEndPos + 1

                // find next expression
                expressionPos = raw.indexOf(START_TOKEN, lastExpressionPos)
            }

            // add remaining part if any
            if (lastExpressionPos < raw.length) {
                fragments.add(raw.substring(lastExpressionPos))
            }

            return fragments
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