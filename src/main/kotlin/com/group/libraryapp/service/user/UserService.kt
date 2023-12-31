package com.group.libraryapp.service.user

import com.group.libraryapp.domain.user.User
import com.group.libraryapp.domain.user.UserRepository
import com.group.libraryapp.domain.user.UserStatus
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistory
import com.group.libraryapp.domain.user.loanhistory.UserLoanStatus
import com.group.libraryapp.dto.user.request.UserCreateRequest
import com.group.libraryapp.dto.user.request.UserUpdateRequest
import com.group.libraryapp.dto.user.response.BookHistoryResponse
import com.group.libraryapp.dto.user.response.UserLoanHistoryResponse
import com.group.libraryapp.dto.user.response.UserResponse
import com.group.libraryapp.util.findByIdOrThrow
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    private val userRepository: UserRepository
) {
    @Transactional
    fun saveUser(request: UserCreateRequest) {
        userRepository.save(User(name = request.name, age = request.age, status = UserStatus.ACTIVE))
    }

    // users를 코틀린의 프로퍼티로 선언, @Transactional은 get() 에 붙일 수 있다..!
    val users
        @Transactional(readOnly = true)
        get() = userRepository.findAll()
//        .map { UserResponse(it) } // 파라미터가 하나인 경우 it 을 활용
//        .map(::UserResponse)      // 생성자를 호출할 때 메소드 파라미터를 활용
            .map { user -> UserResponse.of(user) } // 그러나 명시적으로 람다를 사용하는편이 직관적 이다...

    @Transactional
    fun updateUserName(request: UserUpdateRequest) {
        //        userRepository.findByIdOrNull(request.id)?.updateName(request.name)
        val user = userRepository.findByIdOrThrow(request.id)
        user.updateName(request.name)

    }

    @Transactional
    fun deleteUser(name: String) {
        userRepository.findByName(name)?.apply {
            userRepository.delete(this)
        }
    }

    // 유저의 대출기록을 가져오는 메소드
    @Transactional(readOnly = true)
    fun getUserLoanHistories(): List<UserLoanHistoryResponse> =
//            userRepository.findAllWithHistories().map(UserLoanHistoryResponse::of)
        userRepository.findWithHistoriesQueryDsl().map(UserLoanHistoryResponse::of)     // queryDsl 버전으로 변경
}