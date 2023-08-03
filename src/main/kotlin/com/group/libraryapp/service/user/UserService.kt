package com.group.libraryapp.service.user

import com.group.libraryapp.domain.user.User
import com.group.libraryapp.domain.user.UserRepository
import com.group.libraryapp.dto.user.request.UserCreateRequest
import com.group.libraryapp.dto.user.request.UserUpdateRequest
import com.group.libraryapp.dto.user.response.UserResponse
import com.group.libraryapp.util.fail
import com.group.libraryapp.util.findByIdOrThrow
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService (
    private val userRepository: UserRepository
) {
    @Transactional
    fun saveUser(request: UserCreateRequest){
        userRepository.save(User(request.name, request.age))
    }

    // users를 코틀린의 프로퍼티로 선언, @Transactional은 get() 에 붙일 수 있다..!
    val users
        @Transactional(readOnly = true)
        get() = userRepository.findAll()
//        .map { UserResponse(it) } // 파라미터가 하나인 경우 it 을 활용
//        .map(::UserResponse)      // 생성자를 호출할 때 메소드 파라미터를 활용
            .map { user -> UserResponse(user) } // 그러나 명시적으로 람다를 사용하는편이 직관적이다...

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
}