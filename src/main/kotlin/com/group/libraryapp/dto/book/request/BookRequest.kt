package com.group.libraryapp.dto.book.request

data class BookRequest(
    val name: String,
    val type: String
) {
    companion object {
        fun fixture(
            name: String = "책이름",
            type: String = "책타입"
        ) = BookRequest(name = name, type = type)
    }
}
