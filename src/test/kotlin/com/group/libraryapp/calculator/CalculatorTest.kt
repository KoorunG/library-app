package com.group.libraryapp.calculator

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.random.Random
import kotlin.random.nextInt


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
    fun backjoon2480() {
        // 주사위 경우의 수 생성
        val diceList: MutableList<Int> = mutableListOf<Int>().apply {
            repeat(3) {
                add((Random.nextInt(1..6)))
            }
        }
        // 같은 눈이 3개 나오는 경우 -> 1등 (10000 + 같은 눈 * 1000)
        // 같은 눈이 2개 나오는 경우 -> 2등 (1000 + 같은 눈 * 100)
        // 모두 다른 눈이 나오는경우 -> 3등 (가장 큰 눈 * 100)
        val result = diceList
            .groupBy { it }
            .map { (k, v) -> v.count() to k }
            .sortedByDescending { it.first }
            .sortedByDescending { it.second }
            .first()
            .run {
                when (first) {
                    3 -> 10000 + second * 1000
                    2 -> 1000 + second * 100
                    1 -> second * 100
                    else -> 0
                }
            }
        print(result)
    }

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
            // apply 스코프함수로 에러메세지까지 검증 가능하다!
            .apply {
                assertThat(message).isEqualTo("0으로 나눌 수 없습니다!")
            }
    }

    @Test
    fun `나누기 테스트`() {
        assertCalculatorEquality(6, 2) { divide(3) }
    }
}