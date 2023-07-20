package com.github.thedeathlycow.calyx.kt

import com.github.thedeathlycow.calyx.kt.syntax.ExpressionChain

/**
 * Defines a filter rule for strings.
 *
 * This applies to expanded strings in [ExpressionChain]s as a way of transforming the string according to some rules.
 * For example, converting the string to upper case. The method that this annotates must have a string parameter (which
 * is the input string to transform), an [Options] parameter, and it must return a string.
 *
 * This annotation can only be applied to static methods in Java or, in Kotlin, methods annotated with [JvmStatic]
 *
 * For example:
 *
 * ```kt
 * object MyFilters {
 *      @FilterName("backwards")
 *      @JvmStatic
 *      fun backwards(input: String, options: Options): String {
 *          return input.reversed()
 *      }
 * }
 * ```
 *
 * @see Filters
 */
@Target(AnnotationTarget.FUNCTION)
annotation class FilterName(
    val name: String
)
