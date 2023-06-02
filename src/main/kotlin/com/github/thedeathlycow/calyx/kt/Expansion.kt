package com.github.thedeathlycow.calyx.kt

class Expansion(
    val symbol: Symbol,
    val tail: List<Expansion>,
    val term: String = ""
) {


    constructor(symbol: Symbol, tail: Expansion) : this(symbol, listOf(tail))

    constructor(symbol: Symbol, term: String) : this(symbol, listOf(), term)

    fun flatten(): StringBuilder {
        val concat = StringBuilder()
        this.collectAtoms(concat)
        return concat
    }

    fun collectAtoms(concat: StringBuilder) {
        if (this.symbol == Symbol.ATOM) {
            concat.append(this.term);
        } else {
            this.tail.forEach { exps ->
                exps.collectAtoms(concat)
            }
        }
    }

    enum class Symbol {
        RESULT,
        UNIFORM_BRANCH,
        WEIGHTED_BRANCH,
        EMPTY_BRANCH,
        AFFIX_TABLE,
        TEMPLATE,
        EXPRESSION,
        ATOM,
        MEMO,
        UNIQ
    }

}