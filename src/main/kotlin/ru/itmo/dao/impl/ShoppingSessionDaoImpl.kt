package ru.itmo.dao.impl

import org.springframework.data.jpa.repository.support.SimpleJpaRepository
import org.springframework.stereotype.Repository
import ru.itmo.dao.ShoppingSessionDao
import ru.itmo.model.entity.Cart
import ru.itmo.model.entity.ShoppingSession
import ru.itmo.model.entity.User
import javax.persistence.EntityManager
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.Predicate
import javax.persistence.criteria.Root

@Repository
class ShoppingSessionDaoImpl(
    private val entityManager: EntityManager
) : ShoppingSessionDao, SimpleJpaRepository<ShoppingSession, Long>(ShoppingSession::class.java, entityManager) {

    override fun findActiveCartSession(cartId: Long): ShoppingSession? {
        val cb = entityManager.criteriaBuilder
        val cq = cb.createQuery(ShoppingSession::class.java)

        val root = cq.from(ShoppingSession::class.java)
        cq.select(root)

        val parameters = mapOf(ACTIVE_PARAM to true, "cartId" to cartId)
        val predicate = cb.and(
            createActivePredicate(cb, root),
            cb.equal(
                root.get<Cart>(ShoppingSession.CART).get<Long>(Cart.ID),
                cb.parameter(Long::class.javaObjectType, "cartId")
            )
        )
        cq.where(predicate)

        val query = entityManager.createQuery(cq)
        parameters.forEach { (key, value) -> query.setParameter(key, value) }

        return query.resultList.firstOrNull()
    }

    override fun findActiveUserSession(userId: Long): ShoppingSession? {
        val cb = entityManager.criteriaBuilder
        val cq = cb.createQuery(ShoppingSession::class.java)

        val root = cq.from(ShoppingSession::class.java)
        cq.select(root)

        val parameters = mapOf(ACTIVE_PARAM to true, "userId" to userId)
        val predicate = cb.and(
            createActivePredicate(cb, root),
            cb.equal(
                root.get<Cart>(ShoppingSession.USER).get<Long>(User.ID),
                cb.parameter(Long::class.javaObjectType, "userId")
            )
        )
        cq.where(predicate)

        val query = entityManager.createQuery(cq)
        parameters.forEach { (key, value) -> query.setParameter(key, value) }

        return query.resultList.firstOrNull()
    }

    companion object {
        const val ACTIVE_PARAM = "active"

        private fun createActivePredicate(cb: CriteriaBuilder, root: Root<ShoppingSession>): Predicate {
            val activePath = root.get<Boolean>(ShoppingSession.ACTIVE)
            val activeParameter = cb.parameter(Boolean::class.javaObjectType, ACTIVE_PARAM)
            return cb.equal(activePath, activeParameter)
        }
    }
}