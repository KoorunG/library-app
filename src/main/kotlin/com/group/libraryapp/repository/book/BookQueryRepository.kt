package com.group.libraryapp.repository.book

import com.group.libraryapp.domain.book.QBook.book
import com.group.libraryapp.dto.book.response.BookStatResponse
import com.querydsl.core.types.Projections
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Component

// querydsl용 Repository를 하나의 @Component로 등록해서 사용
// 클래스만 만들면 되기 때문에 간결하다
@Component
class BookQueryRepository(
    private val factory: JPAQueryFactory
) {
    fun groupByTypeQueryDsl() =
        factory
            .select(
                Projections.constructor(
                    BookStatResponse::class.java, // Projection.constructor에 type을 넘겨주려면 이렇게 사용해야함.. (개선되겠지?)
                    book.type,
                    book.id.count().intValue()
                )
            )
            .from(book)
            .groupBy(book.type)
            .fetch()
            .toList()
}