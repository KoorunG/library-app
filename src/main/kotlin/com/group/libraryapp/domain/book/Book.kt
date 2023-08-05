package com.group.libraryapp.domain.book

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class Book(
    @Column(nullable = false)
    val name: String,

    @Column(nullable = false)
    val type: String,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
) {
    // 코틀린의 초기화블록
    init {
        if(name.isBlank()) {
            throw IllegalArgumentException("이름은 비어있을 수 없습니다!")
        }
    }

    // 코틀린에서 컴패니언 오브젝트는 맨 하단에 선언하는것이 컨벤션이다.
    // 테스트에 활용할 디폴트객체를 생생하는 메소드인 fixture()를 컴패니언 오브젝트의 메소드로 선언하면 편하다.
    companion object {
        fun fixture(
            name: String = "책이름",
            type: String = "책타입",
            id: Long? = null
        ) = Book(name = name, type = type, id = id)
    }
}