package com.github.thedeathlycow.calyx.kt

import java.lang.reflect.Method
import java.math.BigDecimal
import kotlin.reflect.KClass

class Registry(
    options: Options? = null
) {

    private val options: Options
    private val rules: MutableMap<String, Rule> = HashMap()
    private val context: MutableMap<String, Rule> = HashMap()
    private val memos: MutableMap<String, Expansion> = HashMap()
    private val cycles: MutableMap<String, Cycle> = HashMap()
    private val filterClasses: MutableList<KClass<*>> = mutableListOf(Filters::class)

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

    fun addFilterClass(filterClass: KClass<*>) {
        this.filterClasses.add(filterClass)
    }

    fun getFilterComponent(label: String): (String) -> String  {
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

        if (filter.getAnnotation(JvmStatic::class.java) === null) {
            throw NonStaticFilter(label)
        }

        return { input ->
            filter.invoke(null, input, options) as String
        }
    }

    fun resetEvaluationContext() {
        this.context.clear()
        this.memos.clear()
        this.cycles.clear()
    }

}