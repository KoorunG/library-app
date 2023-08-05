package com.group.libraryapp.service.book

import com.group.libraryapp.domain.book.Book
import com.group.libraryapp.domain.book.BookRepository
import com.group.libraryapp.domain.user.UserRepository
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistoryRepository
import com.group.libraryapp.dto.book.request.BookLoanRequest
import com.group.libraryapp.dto.book.request.BookRequest
import com.group.libraryapp.dto.book.request.BookReturnRequest
import com.group.libraryapp.util.fail
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class BookService(
    private val bookRepository: BookRepository,
    private val userRepository: UserRepository,
    private val userLoanHistoryRepository: UserLoanHistoryRepository
) {

    @Transactional
    fun saveBook(request: BookRequest) {
        bookRepository.save(Book(name = request.name, type = request.type))
    }

    @Transactional
    fun loanBook(request: BookLoanRequest) {
        val book = bookRepository.findByName(request.bookName) ?: fail("해당 이름의 책이 존재하지 않습니다.")
        val user = userRepository.findByName(request.userName) ?: fail("해당 이름의 유저가 존재하지 않습니다.")
        if(userLoanHistoryRepository.findByBookNameAndIsReturn(request.bookName, false) != null) {
            fail("진작 대출되어 있는 책입니다")
        }
        user.loanBook(book)
    }

    @Transactional
    fun returnBook(request: BookReturnRequest) {
        // findByName의 결과가 null이 아닐 경우 returnBook을 실행 (Safe call 이용)
        userRepository.findByName(request.userName)?.returnBook(request.bookName)
    }
}