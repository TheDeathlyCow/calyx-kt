package com.github.thedeathlycow.calyx.kt

import java.util.*
import java.util.regex.Pattern

@Suppress("unused")
object Filters {

    @FilterName("uppercase")
    @JvmStatic
    fun uppercase(input: String, options: Options): String {
        return input.uppercase(Locale.getDefault())
    }

    @FilterName("lowercase")
    @JvmStatic
    fun lowercase(input: String, options: Options): String {
        return input.lowercase(Locale.getDefault())
    }

    @FilterName("titlecase")
    @JvmStatic
    fun titlecase(input: String, options: Options): String {
        return input.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
    }

    @FilterName("sentencecase")
    @JvmStatic
    fun sentencecase(input: String, options: Options): String {
        val lowerCase = input.lowercase()
        val regex = Pattern.compile("(^[A-Z])|(\\.\\s+(.))", Pattern.MULTILINE)
        return regex.matcher(lowerCase).replaceAll {
            it.group().uppercase()
        }
    }

    @FilterName("length")
    @JvmStatic
    fun length(input: String, options: Options): String {
        return input.length.toString()
    }

    @FilterName("emphasis")
    @JvmStatic
    fun emphasis(input: String, options: Options): String {
        return "*$input*"
    }

}