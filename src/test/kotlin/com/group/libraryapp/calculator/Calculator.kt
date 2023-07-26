package com.group.libraryapp.calculator

class Calculator(
    private val _current: Int) {

    fun add(operand: Int) = _current.run { plus(operand) }

    fun minus(operand: Int) = _current.run { minus(operand) }

    fun multiply(operand: Int) = _current.run { times(operand) }

    fun divide(operand: Int) = _current.run {
        if(operand == 0) throw IllegalArgumentException("0으로 나눌 수 없습니다.")
        div(operand)
    }
}
