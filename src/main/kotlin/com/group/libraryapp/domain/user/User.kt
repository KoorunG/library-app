package com.group.libraryapp.domain.user

import com.group.libraryapp.domain.book.Book
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistory
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.OneToMany

@Entity
class User constructor( // 주생성자에 constuctor 키워드를 붙이면 Entity가 생성되는 코드를 쉽게 추적할 수 있다.
    @Column(nullable = false)
    var name: String,

    val age: Int?,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
) {

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
    val userLoanHistories: MutableList<UserLoanHistory> = mutableListOf()

    init {
        if (name.isBlank()) {
            throw IllegalArgumentException("이름은 비어 있을 수 없습니다")
        }
    }

    fun updateName(name: String) {
        this.name = name
    }

    fun loanBook(book: Book) {
        this.userLoanHistories.add(UserLoanHistory(this, book.name, false))
    }

    // 코틀린의 Lambda를 이용하여 한줄로 처리...
    // (자바에서는 .stream().filter().findFirst().orElseThrow() 로 처리해야했음...
    fun returnBook(bookName: String) {
        this.userLoanHistories.first { it.bookName == bookName }.doReturn()
    }
}