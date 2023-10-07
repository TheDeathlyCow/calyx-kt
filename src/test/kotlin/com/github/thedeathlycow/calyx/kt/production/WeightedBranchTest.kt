package com.github.calyx.kt.production

import com.github.calyx.kt.util.assertNumberWithinPercentRange
import com.github.thedeathlycow.calyx.kt.Options
import com.github.thedeathlycow.calyx.kt.Registry
import com.github.thedeathlycow.calyx.kt.production.WeightedBranch
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import java.math.BigDecimal
import kotlin.test.assertEquals

class WeightedBranchTest {

    @Test
    fun branchWithMultiChoiceTest() {
        val branch = WeightedBranch.parse(
            mapOf(
                "lithium" to 50,
                "silicon" to 30,
                "carbon" to 20
            ),
            Registry()
        )

        val exp = branch.evaluate(Options(seed = 1234))
        assertEquals("carbon", exp.flattenToString())
    }

    @Test
    fun branchWithDoubleWeights() {
        val branch = WeightedBranch.parse(
            mapOf(
                "lithium" to 0.5,
                "silicon" to 0.3,
                "carbon" to 0.2
            ),
            Registry()
        )

        val exp = branch.evaluate(Options(seed = 1234))
        assertEquals("carbon", exp.flattenToString())
    }

    @Test
    fun branchWithBigDecimalWeights() {
        val branch = WeightedBranch.parse(
            mapOf(
                "lithium" to BigDecimal.valueOf(0.5),
                "silicon" to BigDecimal.valueOf(0.3),
                "carbon" to BigDecimal.valueOf(0.2)
            ),
            Registry()
        )

        val exp = branch.evaluate(Options(seed = 1234))
        assertEquals("carbon", exp.flattenToString())
    }

    @Test
    fun resultsRoughlyMatchWeight() {

        val fizzTimes = 700
        val buzzTimes = 300

        val branch = WeightedBranch.parse(
            mapOf(
                "fizz" to fizzTimes,
                "buzz" to buzzTimes
            ),
            Registry()
        )

        val res = (0..(fizzTimes + buzzTimes))
            .map {
                branch.evaluate(Options(seed = it))
            }
            .toList()

        val totalFizz = res.count {
            it.tail[0].tail[0].term == "fizz"
        }
        val totalBuzz = res.count {
            it.tail[0].tail[0].term == "buzz"
        }

        val allowedError = 0.01
        assertNumberWithinPercentRange(fizzTimes, totalFizz, allowedError)
        assertNumberWithinPercentRange(buzzTimes, totalBuzz, allowedError)
    }

    @Test
    fun resultApproximateDescendingWeight() {
        val branch = WeightedBranch.parse(
            mapOf(
                "A" to 5,
                "B" to 3,
                "C" to 2
            ),
            Registry()
        )

        val res = (0..1000)
            .map {
                branch.evaluate(Options(seed = it))
            }
            .toList()

        val countA = res.count {
            it.tail[0].tail[0].term == "A"
        }
        val countB = res.count {
            it.tail[0].tail[0].term == "B"
        }
        val countC = res.count {
            it.tail[0].tail[0].term == "C"
        }


        assertNumberWithinPercentRange(500, countA, 0.01)
        assertNumberWithinPercentRange(300, countB, 0.01)
        assertNumberWithinPercentRange(200, countC, 0.01)
    }

    @Test
    fun resultApproximateAscendingWeight() {
        val branch = WeightedBranch.parse(
            mapOf(
                "C" to 2,
                "B" to 3,
                "A" to 5,
            ),
            Registry()
        )

        val res = (0..1000)
            .map {
                branch.evaluate(Options(seed = it))
            }
            .toList()

        val countA = res.count {
            it.tail[0].tail[0].term == "A"
        }
        val countB = res.count {
            it.tail[0].tail[0].term == "B"
        }
        val countC = res.count {
            it.tail[0].tail[0].term == "C"
        }


        assertNumberWithinPercentRange(500, countA, 0.01)
        assertNumberWithinPercentRange(300, countB, 0.01)
        assertNumberWithinPercentRange(200, countC, 0.01)
    }

    @Test
    fun resultApproximateUniformWeight() {
        val branch = WeightedBranch.parse(
            mapOf(
                "B" to 5,
                "A" to 5,
            ),
            Registry()
        )

        val res = (0..1000)
            .map {
                branch.evaluate(Options(seed = it))
            }
            .toList()

        val countA = res.count {
            it.tail[0].tail[0].term == "A"
        }
        val countB = res.count {
            it.tail[0].tail[0].term == "B"
        }


        assertNumberWithinPercentRange(countA, countB, 0.01)
    }

    @Test
    fun mutlipleValuesOfSameRatio() {
        val branch = WeightedBranch.parse(
            mapOf(
                "A" to 3,
                "B" to 4,
                "C" to 3
            ),
            Registry()
        )

        val res = (0..1000)
            .map {
                branch.evaluate(Options(seed = it))
            }
            .toList()

        val countA = res.count {
            it.tail[0].tail[0].term == "A"
        }
        val countB = res.count {
            it.tail[0].tail[0].term == "B"
        }
        val countC = res.count {
            it.tail[0].tail[0].term == "C"
        }
        assertNumberWithinPercentRange(300, countA, 0.01)
        assertNumberWithinPercentRange(400, countB, 0.01)
        assertNumberWithinPercentRange(300, countC, 0.01)
    }

    @ParameterizedTest
    @ValueSource(
        ints = [
            0, -1, Int.MIN_VALUE
        ]
    )
    fun nonPositiveWeightsAreRejected(testCase: Int) {
        val choices = mapOf(
            "yin" to 10,
            "yang" to testCase
        )

        assertThrows<IllegalArgumentException> {
            WeightedBranch.parse(choices, Registry())
        }
    }

    @ParameterizedTest
    @ValueSource(
        doubles = [
            Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY
        ]
    )
    fun infiniteWeightsAreRejected(testCase: Double) {
        val choices = mapOf(
            "yin" to 1.0,
            "yang" to testCase
        )

        assertThrows<IllegalArgumentException> {
            WeightedBranch.parse(choices, Registry())
        }
    }

    @Test
    fun nanWeightsAreRejected() {
        val testCase: Double = Double.NaN

        val choices = mapOf(
            "yin" to 1.0,
            "yang" to testCase
        )

        assertThrows<IllegalArgumentException> {
            WeightedBranch.parse(choices, Registry())
        }
    }

}