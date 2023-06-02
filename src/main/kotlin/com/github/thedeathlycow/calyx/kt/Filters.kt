package com.github.thedeathlycow.calyx.kt

import java.util.*
import java.util.regex.Pattern

object Filters {

    @FilterName("uppercase")
    fun uppercase(input: String, options: Options): String {
        return input.uppercase(Locale.getDefault())
    }

    @FilterName("lowercase")
    fun lowercase(input: String, options: Options): String {
        return input.lowercase(Locale.getDefault())
    }

    @FilterName("titlecase")
    fun titlecase(input: String, options: Options): String {
        return input.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
    }

    @FilterName("sentencecase")
    fun sentencecase(input: String, options: Options): String {
        val lowerCase = input.lowercase(Locale.getDefault())
        val regex = Pattern.compile("(^[A-Z])|(\\.\\s+(.))", Pattern.MULTILINE)
        return regex.matcher(lowerCase).replaceAll { it.group().uppercase(Locale.getDefault()) }
    }

    @FilterName("length")
    fun length(input: String, options: Options): String {
        return input.length.toString()
    }

    @FilterName("emphasis")
    fun emphasis(input: String, options: Options): String {
        return "*$input*"
    }

}