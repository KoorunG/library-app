package com.group.libraryapp.calculator


/**
 * 계산기 테스트 클래스
 *
 * 1. 계산기는 정수만을 취급한다.
 * 2. 계산기가 생성될 때 숫자를 1개 받는다.
 * 3. 최초 숫자가 기록된 이후에는 연산자 함수를 통해 숫자를 받아 지속적으로 계산한다.
 */
data class Calculator(
    var current: Int
)

fun Calculator.add(operand: Int) {
    this.current += operand
}

fun Calculator.multiply(operand: Int) {
    this.current *= operand
}

fun Calculator.divide(operand: Int) {
    if(operand == 0) throw IllegalArgumentException("0으로 나눌 수 없습니다!")
    this.current /= operand
}

fun Calculator.minus(operand: Int){
    this.current -= operand
}
