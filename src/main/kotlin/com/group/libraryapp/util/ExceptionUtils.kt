package com.group.libraryapp.util

import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.findByIdOrNull

fun fail(message: String? = null): Nothing {
    when(message) {
        null -> throw IllegalArgumentException()
        else -> throw IllegalArgumentException(message)
    }
}

// CrudRepository의 확장함수인 findByIdOrNull 을 다시 한번 확장함수화
fun <T, ID> CrudRepository<T, ID>.findByIdOrThrow(id: ID): T = findByIdOrNull(id) ?: fail()