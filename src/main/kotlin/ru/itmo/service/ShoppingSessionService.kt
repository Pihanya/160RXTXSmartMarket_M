package ru.itmo.service

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import ru.itmo.dao.ShoppingSessionDao
import ru.itmo.model.entity.Cart
import ru.itmo.model.entity.ShoppingSession
import ru.itmo.model.entity.User
import ru.itmo.model.exception.ServiceException
import ru.itmo.model.view.ShoppingSessionSummaryView
import javax.transaction.Transactional

@Service
class ShoppingSessionService(
    private val shoppingSessionDao: ShoppingSessionDao
) {

    @Transactional
    fun startSession(cartId: Long, userId: Long) {
        val activeUserSession = shoppingSessionDao.findActiveUserSession(userId)
        if (activeUserSession != null) {
            throw ServiceException("User already has an active session")
        }

        val activeCartSession = shoppingSessionDao.findActiveCartSession(cartId)
        if (activeCartSession != null) {
            throw ServiceException("Cart is already busy in another shopping session")
        }

        val newSession = shoppingSessionDao.save(
            ShoppingSession(
                cart = Cart(id = cartId),
                user = User(id = userId), active = true
            )
        )

        LOGGER.info("Shopping session #{} of User #{} has started for Cart #{}", newSession.id, userId, cartId)
    }

    @Transactional
    fun releaseSession(userId: Long, cartId: Long): ShoppingSession? {
        val activeSession = shoppingSessionDao.findActiveUserSession(userId) ?: return null

        activeSession.active = false
        return shoppingSessionDao.save(activeSession)
    }

    @Transactional
    fun endSession(userId: Long, cartId: Long) { // TODO Implement money flow
        val activeSession = shoppingSessionDao.findActiveUserSession(userId)
            ?: throw ServiceException("Could not find active shopping session")

        activeSession.active = false
        shoppingSessionDao.save(activeSession)
    }

    @Transactional
    fun findActiveSessionSummaryView(userId: Long): ShoppingSessionSummaryView? {
        val activeSession = shoppingSessionDao.findActiveUserSession(userId) ?: return null

        val cartId = checkNotNull(activeSession.cart?.id)
        val sessionStartDate = checkNotNull(activeSession.creationDate)

        return ShoppingSessionSummaryView(cartId, sessionStartDate)
    }

    companion object {
        private val LOGGER: Logger = LoggerFactory.getLogger(ShoppingSessionService::class.java)
    }
}