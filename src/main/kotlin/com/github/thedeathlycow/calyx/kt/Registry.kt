package com.github.thedeathlycow.calyx.kt

import java.lang.reflect.Method
import kotlin.reflect.KClass

class Registry(
    options: Options? = null
) {

    private val options: Options
    private val rules: MutableMap<String, Rule> = HashMap()
    private val context: MutableMap<String, Rule> = HashMap()
    private val memos: MutableMap<String, Expansion> = HashMap()
    private val cycles: MutableMap<String, Cycle> = HashMap()
    private val filterClasses: List<KClass<out Filters>> = listOf<KClass<out Filters>>(Filters::class)

    init {
        this.options = options ?: Options()
    }

    fun memoizeExpansion(symbol: String): Expansion {
        if (!memos.containsKey(symbol)) {
            memos[symbol] = this.expand(symbol).evaluate(this.options)
        }

        return memos.getValue(symbol)
    }

    fun uniqueExpansion(symbol: String): Expansion {
        // If this symbol has not been expanded as a cycle then register it
        if (!this.cycles.containsKey(symbol)) {
            val prod = this.expand(symbol)
            val cycleLength = prod.length
            cycles[symbol] = Cycle(options, cycleLength)
        }

        return this.expand(symbol)
            .evaluateAt(
                cycles.getValue(symbol).poll(),
                options
            )
    }

    fun evaluate(startSymbol: String): Expansion {
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

    fun getFilterComponent(label: String): Function1<String, String> {
        val filter: Method? = filterClasses
            .flatMap { filterClass ->
                filterClass.java.methods.filter { method ->
                    val annotation: FilterName? = method.getAnnotation(FilterName::class.java)
                    annotation !== null && annotation.name == label
                }
            }
            .firstOrNull()

        if (filter === null) {
            throw UndefinedFilter(label)
        }

        return { input ->
            filter.invoke(Filters, arrayOf(input, options)) as String
        }
    }


    fun resetEvaluationContext() {
        this.context.clear()
        this.memos.clear()
        this.cycles.clear()
    }

}