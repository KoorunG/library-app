package com.group.libraryapp.calculator

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

fun assertCalculatorEquality(
    before: Int,
    after: Int,
    oper: Calculator.() -> Unit
) {
    // give
    val calculator = Calculator(before)
    // when
    calculator.oper()
    val expectedCalculator = Calculator(after)
    // then
    assertThat(calculator).isEqualTo(expectedCalculator)
}

class CalculatorTest {

    @Test
    fun `더하기 테스트`() {
        assertCalculatorEquality(5, 8) { add(3) }
    }

    @Test
    fun `빼기 테스트`() {
        assertCalculatorEquality(5, 2) { minus(3) }
    }

    @Test
    fun `곱하기 테스트`() {
        assertCalculatorEquality(5, 15) { multiply(3) }
    }

    @Test
    fun `0으로 나누면 에러가 발생한다`() {
        val calculator = Calculator(5)
        assertThrows<IllegalArgumentException> { calculator.divide(0) }
    }

    @Test
    fun `나누기 테스트`() {
        assertCalculatorEquality(6, 2) { divide(3) }
    }
}