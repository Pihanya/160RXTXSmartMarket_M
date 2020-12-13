package ru.itmo.dao

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.itmo.model.entity.ShoppingSession

@Repository
interface ShoppingSessionDao: JpaRepository<ShoppingSession, Long> {

    fun findActiveCartSession(cartId: Long): ShoppingSession?

    fun findActiveUserSession(userId: Long): ShoppingSession?
}