package com.github.thedeathlycow.calyx.kt

class Registry(
    options: Options? = null
) {

    private val options: Options
    private val rules: MutableMap<String, Rule> = HashMap()
    private val context: MutableMap<String, Rule> = HashMap()
    private val memos: MutableMap<String, Expansion> = HashMap()

    init {
        this.options = options ?: Options()
    }

    fun evalulate(startSymbol: String): Expansion {
        this.resetEvaluationContext()

        return Expansion(
            Expansion.Symbol.RESULT,
            expand(startSymbol).evaluate(options)
        )
    }

    fun evaluate(startSymbol: String, context: Map<String, Array<String>>): Expansion {
        this.resetEvaluationContext()

        context.forEach { rule ->
            // TODO: Rule#build
            //this.context[rule.key] = Rule.build(rule.key, rule.value, this)
        }

        return Expansion(
            Expansion.Symbol.RESULT,
            expand(startSymbol).evaluate(options)
        )
    }

    fun expand(symbol: String): Rule {
        val production: Rule

        if (rules.containsKey(symbol)) {
            production = this.rules.getValue(symbol)
        } else if (context.containsKey(symbol)) {
            production = this.context.getValue(symbol)
        } else {
            if (this.options.isStrict) {
                throw UndefinedRule(symbol)
            }

            production = Rule.empty(symbol)
        }

        return production
    }


    fun resetEvaluationContext() {
        this.context.clear()
        this.memos.clear()
    }

}