package com.group.libraryapp.domain.user.loanhistory

import com.group.libraryapp.domain.user.User
import javax.persistence.*

@Entity
class UserLoanHistory constructor(

    @ManyToOne
    val user: User,
    val bookName: String,

    @Enumerated(EnumType.STRING)
    var status: UserLoanStatus = UserLoanStatus.LOANED,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
) {
    fun doReturn() {
        this.status = UserLoanStatus.RETURNED
    }

    val isReturn
        get() = this.status == UserLoanStatus.RETURNED


    companion object {
        fun fixture(
            user: User,
            bookName: String,
            status: UserLoanStatus,
        ) = UserLoanHistory(user = user, bookName = bookName, status = status)
    }
}