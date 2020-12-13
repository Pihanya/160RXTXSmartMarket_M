package ru.itmo.dao

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.itmo.model.entity.User

@Repository
interface UserDao: JpaRepository<User, Long> {

    fun findByEmail(email: String): User?
}