package com.group.libraryapp

import org.junit.jupiter.api.*


class JunitTest {

    // 코틀린에서 @BeforeAll, @AfterAll을 사용할 때는
    // companion object 안에 메소드를 선언하고 @JvnStatic을 붙여줘야 한다.
    companion object {
        @JvmStatic
        @BeforeAll
        fun init() {
            println("테스트 시작 전 딱 한번 실행!")
        }

        @JvmStatic
        @AfterAll
        fun cleanup() {
            println("테스트 종료 후 딱 한번 실행!")
        }
    }

    @BeforeEach
    fun beforeTest() {
        println("각 테스트가 실행되기 전 호출")
    }

    @AfterEach
    fun afterTest() {
        println("각 테스트가 실행된 후 호출")
    }

    @Test
    fun test1() {
        println("테스트 1 실행")
    }

    @Test
    fun test2() {
        println("테스트 2 실행")
    }

    @Test
    fun test3() {
        println("테스트 3 실행")
    }
}