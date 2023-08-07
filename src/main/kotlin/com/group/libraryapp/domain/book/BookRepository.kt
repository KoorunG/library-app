package com.group.libraryapp.domain.book

import com.group.libraryapp.dto.book.response.BookStatResponse
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import javax.persistence.Tuple

interface BookRepository: JpaRepository<Book, Long> {
    fun findByName(bookName: String): Book?

    // 1. 쿼리의 결과로 Tuple을 가져오는 방법
    // 서비스단에서 Tuple -> Response로 변환해줘야 하기 때문에 번거로울 수 있다.
    @Query("select b.type as type, count(1) as count from Book b group by b.type")
    fun groupByType(): List<Tuple>

    // 2. 바로 DTO로 변환하는 방법
    // 단, count(..)의 결과가 Long이기 때문에 Response의 타입을 Int에서 Long으로 바꿔줘야 한다
//    @Query("select new com.group.libraryapp.dto.book.response.BookStatResponse(b.type, count(b.id)) from Book b group by b.type")
//    fun groupByType2(): List<BookStatResponse>
}