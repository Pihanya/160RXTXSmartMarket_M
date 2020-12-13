package ru.itmo.dao

import org.springframework.data.jpa.repository.JpaRepository
import ru.itmo.model.entity.Product

interface ProductDao: JpaRepository<Product, Long> {
}