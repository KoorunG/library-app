package com.group.libraryapp.dto.book.request

import com.group.libraryapp.domain.book.BookType

data class BookRequest(
    val name: String,
    val type: BookType
) {
    companion object {
        fun fixture(
            name: String = "책이름",
            type: BookType = BookType.COMPUTER
        ) = BookRequest(name = name, type = type)
    }
}
