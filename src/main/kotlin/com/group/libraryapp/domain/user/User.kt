package com.group.libraryapp.domain.user

import com.group.libraryapp.domain.book.Book
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistory
import com.group.libraryapp.domain.user.loanhistory.UserLoanStatus
import com.group.libraryapp.dto.user.response.UserLoanHistoryResponse
import javax.persistence.*

@Entity
class User constructor( // 주생성자에 constuctor 키워드를 붙이면 Entity가 생성되는 코드를 쉽게 추적할 수 있다.
    @Column(nullable = false)
    var name: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val status: UserStatus,

    val age: Int? = null,

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
    val userLoanHistories: MutableList<UserLoanHistory> = mutableListOf(),

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
) {

    init {
        if (name.isBlank()) {
            throw IllegalArgumentException("이름은 비어 있을 수 없습니다")
        }
    }

    fun updateName(name: String) {
        this.name = name
    }

    fun loanBook(book: Book) {
        this.userLoanHistories.add(UserLoanHistory(this, book.name, UserLoanStatus.LOANED))
    }

    // 코틀린의 Lambda를 이용하여 한줄로 처리...
    // (자바에서는 .stream().filter().findFirst().orElseThrow() 로 처리해야했음...
    fun returnBook(bookName: String) {
        this.userLoanHistories.first { it.bookName == bookName }.doReturn()
    }

    companion object {
        fun fixture(
            name: String = "테스트유저",
            status: UserStatus = UserStatus.ACTIVE,
            age: Int? = null,
        ) = User(name = name, age = age, status = status)
    }
}