package com.group.libraryapp.service.user

import com.group.libraryapp.domain.book.Book
import com.group.libraryapp.domain.book.BookRepository
import com.group.libraryapp.domain.user.User
import com.group.libraryapp.domain.user.UserRepository
import com.group.libraryapp.domain.user.UserStatus
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistory
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistoryRepository
import com.group.libraryapp.domain.user.loanhistory.UserLoanStatus
import com.group.libraryapp.dto.user.request.UserCreateRequest
import com.group.libraryapp.dto.user.request.UserUpdateRequest
import com.group.libraryapp.dto.user.response.UserLoanHistoryResponse
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.Rollback
import org.springframework.transaction.annotation.Transactional

// 동일 패키지의 자바코드를
// 코틀린코드로 테스트

@SpringBootTest
// 테스트에서는 @Autowired를 명시해주어야 주입이 됨..
// constructor 키워드를 활용하여 @Autowired를 붙이면 편함
class UserServiceTest @Autowired constructor(
    private val userRepository: UserRepository,
    private val bookRepository: BookRepository,
    private val userLoanHistoryRepository: UserLoanHistoryRepository,
    private val userService: UserService
) {

    //     각 테스트가 실행된 이후 DB 클리어
    //     (각 테스트는 컨텍스트를 공유하기 때문에 원하는 결과가 나오지 않는 경우가 있음)
    @AfterEach
    fun cleanup() {
        println("========= CLEAN 시작 ==========")
        userRepository.deleteAll()
    }

    @Test
    fun saveUser() {
        // given
        val request = UserCreateRequest(name = "쿠렁", age = null)
        val user = User(name = request.name, age = request.age, status = UserStatus.ACTIVE)
        // when
        val saved = userRepository.save(user)
        val all = userRepository.findAll()
        // then
        assertThat(saved).isEqualTo(user)
        // DB에서 꺼낸 값을 기준으로 판별
        assertThat(all).hasSize(1)
        assertThat(all).extracting("name").containsExactly("쿠렁")
        assertThat(all).extracting("age").containsExactly(null)
    }

    @Test
    fun `유저 정보 가져오기`() {
        // given - 테스트용 유저 정보 Repository에 저장
        userRepository.saveAll(
            listOf(
                User.fixture(name = "A", age = 20),
                User.fixture(name = "B", age = null),
            )
        )
        // when
        val results = userService.users
        // then
        assertThat(results).hasSize(2)
        assertThat(results).extracting("name").containsExactlyInAnyOrder("A", "B")
        assertThat(results).extracting("age").containsExactlyInAnyOrder(20, null)
    }

    @Test
    fun `유저 정보 업데이트하기`() {
        // given
        val savedUser = userRepository.save(User.fixture(name = "A", age = null))
        val request =
            UserUpdateRequest(savedUser.id!!, "B")    // 엔티티에서는 nullable 타입으로 선언됐으나, PK이기 때문에 절대 null이 아니므로 단언문으로 처리한다.
        // when
        userService.updateUserName(request)
        // then
        val results = userService.users
        assertThat(results).hasSize(1)
        assertThat(results).extracting("name").containsExactlyInAnyOrder("B")
        assertThat(results).extracting("age").containsExactlyInAnyOrder(null)
    }

    @Test
    fun `유저 삭제하기`() {

        // given
        val userA = User.fixture(name = "A", age = 30)
        val userB = User.fixture(name = "B", age = 25)
        userRepository.saveAll(listOf(userA, userB))

        // when
        userService.deleteUser("A")
        userService.deleteUser("B")

        // then
        val results = userService.users
        assertThat(results).isEmpty()
    }

    @Test
    fun `책을 한권도 빌리지 않은 유저도 응답이 넘어와야 한다`() {
        // given
        val user = User.fixture("쿠렁")
        val book1 = Book.fixture("카라마조프의 형제들1")
        userRepository.save(user)
        bookRepository.save(book1)

        // when
        val history = userService.getUserLoanHistories()
        // then
        assertThat(history).hasSize(1)  // 응답이 제대로 넘어오는지
        assertThat(history[0].name).isEqualTo("쿠렁") // 유저정보가 제대로 등록됐는지
        assertThat(history[0].books).isEmpty()  // books 컬렉션이 비어있는지
    }

    @Test
    fun `여러권의 책을 빌린 경우 적절한 응답이 넘어오는지 확인한다`() {
        // given
        val user = User.fixture("쿠렁")
//        val user2 = User.fixture("쿠렁2")
//        val book3 = Book.fixture("안나 카레니나 1")
//        val book4 = Book.fixture("안나 카레니나 2")

        userRepository.save(user)

        val history1 = UserLoanHistory.fixture(user, "카라마조프의 형제들1", UserLoanStatus.LOANED)
        val history2 = UserLoanHistory.fixture(user, "카라마조프의 형제들2", UserLoanStatus.LOANED)
        val history3 = UserLoanHistory.fixture(user, "카라마조프의 형제들3", UserLoanStatus.RETURNED)

        userLoanHistoryRepository.saveAll(listOf(history1, history2, history3))

        // when
        val histories = userService.getUserLoanHistories()

        // then
        assertThat(histories).hasSize(1)    // user별로 응답이 날아와야 한다.
        assertThat(histories).extracting("name").containsExactlyInAnyOrder("쿠렁") // user의 정보가 적절하게 들어있어야 한다.

        assertThat(histories[0].name).isEqualTo("쿠렁")
        assertThat(histories[0].books).hasSize(3)
        assertThat(histories[0].books).extracting("name")
            .containsExactlyInAnyOrder("카라마조프의 형제들1", "카라마조프의 형제들2", "카라마조프의 형제들3")
        assertThat(histories[0].books).extracting("isReturn").containsExactlyInAnyOrder(false, false, true)
    }
}