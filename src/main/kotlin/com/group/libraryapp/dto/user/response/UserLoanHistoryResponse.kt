package com.group.libraryapp.dto.user.response

import com.group.libraryapp.domain.user.User
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistory
import com.group.libraryapp.domain.user.loanhistory.UserLoanStatus

data class UserLoanHistoryResponse(
    val name: String,
    val books: List<BookHistoryResponse>
) {
    companion object {
        fun of(user: User): UserLoanHistoryResponse =
            UserLoanHistoryResponse(user.name, user.userLoanHistories.map { BookHistoryResponse.of(it) })
    }
}

data class BookHistoryResponse(
    val name: String,
    val isReturn: Boolean
) {
    companion object {
        fun of(history: UserLoanHistory): BookHistoryResponse =
            BookHistoryResponse(history.bookName, history.isReturn)
    }
}