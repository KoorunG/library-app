package com.group.libraryapp.config

import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.persistence.EntityManager

@Configuration
class QueryDslConfig(
    private val em: EntityManager
) {
    // queryDsl을 사용하기 위한 JpaQueryFactory Bean 등록
    @Bean
    fun queryDsl(): JPAQueryFactory = JPAQueryFactory(em)
}