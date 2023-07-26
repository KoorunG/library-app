package com.group.libraryapp.calculator

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

/**
 * 계산기 테스트 클래스
 *
 * 1. 계산기는 정수만을 취급한다.
 * 2. 계산기가 생성될 때 숫자를 1개 받는다.
 * 3. 최초 숫자가 기록된 이후에는 연산자 함수를 통해 숫자를 받아 지속적으로 계산한다.
 */
class CalculatorTest {

    @Test
    fun `더하기 테스트`() {
        // give
        val calculator = Calculator(5)
        // when
        val result = calculator.add(3)
        // then
        assertThat(result).isEqualTo(8)
    }

    @Test
    fun `빼기 테스트`() {
        val calculator = Calculator(10)
        val result = calculator.minus(5)
        assertThat(result).isEqualTo(5)
    }

    @Test
    fun `곱하기 테스트`() {
        val calculator = Calculator(5)
        val result = calculator.multiply(3)
        assertThat(result).isEqualTo(15)
    }

    @Test
    fun `0으로 나누면 에러가 발생한다`() {
        val calculator = Calculator(5)
        assertThrows<IllegalArgumentException> { calculator.divide(0) }
    }

    @Test
    fun `나누기 테스트`() {
        val calculator = Calculator(10)
        val result = calculator.divide(5)
        assertThat(result).isEqualTo(2)
    }
}