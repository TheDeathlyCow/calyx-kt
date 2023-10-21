package com.github.thedeathlycow.calyx.kt

import java.lang.reflect.Method
import java.math.BigDecimal

class Registry(
    options: Options? = null
) {

    private val options: Options
    private val rules: MutableMap<String, Rule> = HashMap()
    private val context: MutableMap<String, Rule> = HashMap()
    private val memos: MutableMap<String, Expansion> = HashMap()
    private val cycles: MutableMap<String, Cycle> = HashMap()
    private val filters: FilterRegistry = FilterRegistry().apply {
        addFilters(Filters)
    }

    init {
        this.options = options ?: Options()
    }

    fun defineRule(name: String, productions: List<String>) {
        val rule = Rule.build(name, productions, this)
        this.rules[name] = rule
    }

    fun defineRule(name: String, productions: Map<String, Int>) {
        val rule = Rule.build(name, productions, this)
        this.rules[name] = rule
    }

    @JvmName("defineRuleDouble")
    fun defineRule(name: String, productions: Map<String, Double>) {
        val rule = Rule.build(name, productions, this)
        this.rules[name] = rule
    }

    @JvmName("defineRuleDecimal")
    fun defineRule(name: String, productions: Map<String, BigDecimal>) {
        val rule = Rule.build(name, productions, this)
        this.rules[name] = rule
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
            val cycleLength = prod.size
            cycles[symbol] = Cycle(cycleLength)
        }

        return this.expand(symbol)
            .evaluateAt(
                cycles.getValue(symbol).poll(options),
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

    fun evaluate(startSymbol: String, context: Map<String, List<String>>): Expansion {
        this.resetEvaluationContext()

        context.forEach {
            this.context[it.key] = Rule.build(it.key, it.value, this)
        }

        return Expansion(
            Expansion.Symbol.RESULT,
            expand(startSymbol).evaluate(options)
        )
    }

    fun expand(symbol: String): Rule {
        return if (rules.containsKey(symbol)) {
            this.rules.getValue(symbol)
        } else if (context.containsKey(symbol)) {
            this.context.getValue(symbol)
        } else {
            if (this.options.isStrict) {
                throw UndefinedRule(symbol)
            }
            Rule.empty(symbol)
        }
    }

    fun addFilters(filterInstance: Any) {
        this.filters.addFilters(filterInstance)
    }

    fun getFilterComponent(label: String): (String, Options) -> String  {
        return filters[label]
    }

    fun resetEvaluationContext() {
        this.context.clear()
        this.memos.clear()
        this.cycles.clear()
    }

}