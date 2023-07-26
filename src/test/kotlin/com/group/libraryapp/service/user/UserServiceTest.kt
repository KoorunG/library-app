package com.group.libraryapp.service.user

import com.group.libraryapp.domain.user.User
import com.group.libraryapp.domain.user.UserRepository
import com.group.libraryapp.dto.user.request.UserCreateRequest
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

// 동일 패키지의 자바코드를
// 코틀린코드로 테스트

@SpringBootTest
// 테스트에서는 @Autowired를 명시해주어야 주입이 됨..
// constructor 키워드를 활용하여 @Autowired를 붙이면 편함
class UserServiceTest @Autowired constructor(
    private val userRepository: UserRepository,
    private val userService: UserService,
) {
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
}