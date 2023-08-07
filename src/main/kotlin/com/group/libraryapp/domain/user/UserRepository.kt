package com.group.libraryapp.domain.user

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface UserRepository: JpaRepository<User, Long>, UserRepositoryCustom {
    fun findByName(name: String): User?

    // UserLoanHistory를 같이 가져오는 쿼리
    @Query(value = "select distinct u from User u left join fetch u.userLoanHistories")
    fun findAllWithHistories(): List<User>
}