package ru.itmo.dao

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.itmo.model.entity.Cart

@Repository
interface CartDao: JpaRepository<Cart, Long>