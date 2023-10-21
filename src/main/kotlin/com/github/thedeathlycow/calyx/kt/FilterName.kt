package com.github.thedeathlycow.calyx.kt

import com.github.thedeathlycow.calyx.kt.syntax.ExpressionChain

/**
 * Defines a filter rule for strings.
 *
 * This applies to expanded strings in [ExpressionChain]s as a way of transforming the string according to some rules.
 * For example, converting the string to upper case. The method that this annotates must have a string parameter (which
 * is the input string to transform), an [Options] parameter, and it must return a string.
 *
 * For example:
 *
 * ```kt
 * object MyFilters {
 *      @FilterName("backwards")
 *      fun backwards(input: String, options: Options): String {
 *          return input.reversed()
 *      }
 * }
 * ```
 *
 * In order to register a filter to a Grammar, pass an instance of the containing class to [Grammar.filters].
 *
 * @param name The name of the filter
 * @see Filters
 */
@Target(AnnotationTarget.FUNCTION)
annotation class FilterName(
    val name: String
)
