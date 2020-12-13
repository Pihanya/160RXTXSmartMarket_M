package ru.itmo.dao

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.itmo.model.entity.SessionProduct

@Repository
interface SessionProductDao: JpaRepository<SessionProduct, Long> {

    fun findByProduct_IdAndSession_Id(productId: Long, sessionId: Long): SessionProduct?

    fun findBySession_Id(sessionId: Long): List<SessionProduct>
}