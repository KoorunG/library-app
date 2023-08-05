package com.group.libraryapp.service.book

import com.group.libraryapp.domain.book.Book
import com.group.libraryapp.domain.book.BookRepository
import com.group.libraryapp.domain.user.User
import com.group.libraryapp.domain.user.UserRepository
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistory
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistoryRepository
import com.group.libraryapp.dto.book.request.BookLoanRequest
import com.group.libraryapp.dto.book.request.BookRequest
import com.group.libraryapp.dto.book.request.BookReturnRequest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class BookServiceTest @Autowired constructor(
    private val bookRepository: BookRepository,
    private val userRepository: UserRepository,
    private val userLoanHistoryRepository: UserLoanHistoryRepository,
    private val bookService: BookService
) {
    @AfterEach
    fun `DB 초기화`() {
        bookRepository.deleteAll()
        userRepository.deleteAll()  // 여기서 userLoadHistoryRepository는 자동으로 제거된다 (orphanRemoval 옵션 true로 설정되어있는 상태..)
    }

    @Test
    fun `책 저장하기`() {
        // given
        val request = BookRequest.fixture("도둑맞은 집중력")

        // when
        bookService.saveBook(request)

        // then
        val books = bookRepository.findAll()
        assertThat(books).hasSize(1)
        assertThat(books).extracting("name").containsExactly("도둑맞은 집중력")
    }

    @Test
    fun `책 대출 정상동작`() {
        // given
        bookRepository.saveAll(
            listOf(
                Book.fixture("도둑맞은 집중력"),
                Book.fixture("카라마조프의 형제들1"),
                Book.fixture("사람의 목소리는 빛보다 멀리간다")
            )
        )
        val savedUser = userRepository.save(User("쿠렁", 30))

        // when
        bookService.loanBook(BookLoanRequest("쿠렁", "카라마조프의 형제들1"))

        // then
        val results = userLoanHistoryRepository.findAll()
        assertThat(results).hasSize(1)
        assertThat(results).extracting("bookName").containsExactlyInAnyOrder("카라마조프의 형제들1")
        assertThat(results).extracting("user").extracting("id").containsExactlyInAnyOrder(savedUser.id)
        assertThat(results).extracting("isReturn").containsExactlyInAnyOrder(false)
    }

    @Test
    fun `책이 대출되어 있다면 신규 대출은 실패한다`() {
        // given
        bookRepository.saveAll(
            listOf(
                Book.fixture("도둑맞은 집중력"),
                Book.fixture("카라마조프의 형제들1"),
                Book.fixture("사람의 목소리는 빛보다 멀리간다")
            )
        )
        val savedUser = userRepository.save(User("쿠렁", 30))
        userLoanHistoryRepository.save(
            UserLoanHistory(
                savedUser,
                "카라마조프의 형제들1",
                false
            )
        ) // 이미 대출상태로 설정

        // when - then
        val message = assertThrows<IllegalArgumentException> {
            bookService.loanBook(BookLoanRequest("쿠렁", "카라마조프의 형제들1"))
        }.message

        assertThat(message).isEqualTo("진작 대출되어 있는 책입니다")
    }

    @Test
    fun `책 반납 성공하기`() {
        // given
        bookRepository.saveAll(
            listOf(
                Book.fixture("도둑맞은 집중력"),
                Book.fixture("카라마조프의 형제들1"),
                Book.fixture("사람의 목소리는 빛보다 멀리간다")
            )
        )
        val savedUser = userRepository.save(User("쿠렁", 30))
        userLoanHistoryRepository.save(
            UserLoanHistory(
                savedUser,
                "카라마조프의 형제들1",
                false
            )
        ) // 이미 대출상태로 설정

        // when
        bookService.returnBook(BookReturnRequest("쿠렁", "카라마조프의 형제들1"))

        // then
        val results = userLoanHistoryRepository.findAll()
        assertThat(results).hasSize(1)
        assertThat(results).extracting("bookName").containsExactlyInAnyOrder("카라마조프의 형제들1")
        assertThat(results).extracting("user").extracting("id").containsExactlyInAnyOrder(savedUser.id)
        assertThat(results).extracting("isReturn").containsExactlyInAnyOrder(true)
    }
}