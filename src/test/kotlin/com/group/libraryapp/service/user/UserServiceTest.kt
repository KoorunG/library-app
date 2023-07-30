package com.group.libraryapp.service.user

import com.group.libraryapp.domain.user.User
import com.group.libraryapp.domain.user.UserRepository
import com.group.libraryapp.dto.user.request.UserCreateRequest
import com.group.libraryapp.dto.user.request.UserUpdateRequest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import javax.transaction.Transactional

// 동일 패키지의 자바코드를
// 코틀린코드로 테스트

@SpringBootTest
// 테스트에서는 @Autowired를 명시해주어야 주입이 됨..
// constructor 키워드를 활용하여 @Autowired를 붙이면 편함
class UserServiceTest @Autowired constructor(
    private val userRepository: UserRepository,
    private val userService: UserService,
) {

    // 각 테스트가 실행된 이후 DB 클리어
    // (각 테스트는 컨텍스트를 공유하기 때문에 원하는 결과가 나오지 않는 경우가 있음)
    @AfterEach
    fun cleanup() {
        userRepository.deleteAll()
    }

    @Test
    fun saveUser() {
        // given
        val request = UserCreateRequest("쿠렁", null)
        val user = User(request.name, request.age)
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
                User("A", 20),
                User("B", null),
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
        val savedUser = userRepository.save(User("A", null))
        val request = UserUpdateRequest(savedUser.id, "B")
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
        val userA = User("A", 30)
        val userB = User("B", 25)
        userRepository.saveAll(listOf(userA, userB))

        // when
        userService.deleteUser("A")
        userService.deleteUser("B")

        // then
        val results = userService.users
        assertThat(results).isEmpty()
    }
}