package com.github.thedeathlycow.calyx.kt

import kotlin.test.Test

class GrammarDSLTest {

    @Test
    fun testCreateGrammarDSL() {

        val grammar = Grammar {
            start("{greeting} {adj} world")
            rule("greeting") {
                uniformBranch {
                    item("Hello")
                    item("Hey")
                    items("Yo", "Hi")
                }
            }
            rule("adj") {
                weightedBranch {
                    item("sad", 20.0)
                    items(
                        "happy" to 50.0,
                        "cruel" to 20.0
                    )
                }
            }
        }

    }

}