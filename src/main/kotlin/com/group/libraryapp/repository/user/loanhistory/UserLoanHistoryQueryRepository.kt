package com.group.libraryapp.repository.user.loanhistory

import com.group.libraryapp.domain.user.loanhistory.QUserLoanHistory
import com.group.libraryapp.domain.user.loanhistory.QUserLoanHistory.*
import com.group.libraryapp.domain.user.loanhistory.UserLoanStatus
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Component

@Component
class UserLoanHistoryQueryRepository(
    private val query: JPAQueryFactory
) {

    fun find(bookName: String, status: UserLoanStatus? = null) =
        query
            .select(userLoanHistory)
            .from(userLoanHistory)
            .where(userLoanHistory.bookName.eq(bookName),
                status?.let { userLoanHistory.status.eq(it) }) // status가 존재하는 경우에만 where문에 조건으로 넘겨준다.
            .limit(1)
            .fetchOne()

    fun count(status: UserLoanStatus) =
        query
            .select(userLoanHistory.count())
            .from(userLoanHistory)
            .where(userLoanHistory.status.eq(status))
            .fetchOne()?.toInt() ?: 0   // null일 경우 0 리턴
}