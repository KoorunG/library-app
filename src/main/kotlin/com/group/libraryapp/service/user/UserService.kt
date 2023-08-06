package com.group.libraryapp.service.user

import com.group.libraryapp.domain.user.User
import com.group.libraryapp.domain.user.UserRepository
import com.group.libraryapp.domain.user.UserStatus
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
            .map { user -> UserResponse.of(user) } // 그러나 명시적으로 람다를 사용하는편이 직관적이다...

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
        // 1. userRepository의 모든 유저 목록을 가져온다.
        userRepository.findAll().map { user ->
            // 2. User -> UserLoanHistoryResponse로 매핑한다.
            UserLoanHistoryResponse(
                name = user.name,
                // 3. 여기서 books :: user에 걸린 책 대출내역을 가져와야 하므로 다시 map을 사용하여 매핑해야한다.
                books = user.userLoanHistories.map { history ->
                    BookHistoryResponse(
                        name = history.bookName,
                        // 4. 대출기록의 status를 Enum으로 관리하고 있기때문에 풀어준다.
                        isReturn = history.status == UserLoanStatus.RETURNED
                    )
                })
        }
}