package ru.itmo.service

import org.springframework.stereotype.Service
import ru.itmo.dao.CartDao
import ru.itmo.dao.ProductDao
import ru.itmo.dao.SessionProductDao
import ru.itmo.dao.ShoppingSessionDao
import ru.itmo.model.entity.SessionProduct
import ru.itmo.model.exception.ServiceException
import ru.itmo.model.view.CartStateView
import ru.itmo.model.view.CartStateView.CartState
import ru.itmo.model.view.ProductView
import javax.transaction.Transactional

@Service
class CartService(
    private val cartDao: CartDao,
    private val productDao: ProductDao,
    private val shoppingSessionDao: ShoppingSessionDao,
    private val sessionProductDao: SessionProductDao
) {

    @Transactional
    fun getCartState(cartId: Long, userId: Long): CartStateView {
        val cart = cartDao.findById(cartId).takeIf { it.isPresent }?.get() ?: run {
            throw ServiceException("Requested cart does not exist")
        }

        val cartSession = shoppingSessionDao.findActiveCartSession(checkNotNull(cart.id))
        if (cartSession == null) {
            return CartStateView(cartId = cartId, emptyList(), state = CartState.FREE)
        }

        val sessionUser = checkNotNull(cartSession.user)
        if (userId != sessionUser.id) {
            return CartStateView(cartId = cartId, null, state = CartState.BUSY)
        }

        val productViews = sessionProductDao.findBySession_Id(checkNotNull(cartSession.id)).map { sessionProduct ->
            val product = checkNotNull(sessionProduct.product)
            val quantity = checkNotNull(sessionProduct.quantity)

            ProductView(id = product.id, name = product.name, quantity = quantity)
        }

        return CartStateView(cartId = cartId, products = productViews, state = CartState.OWNED)
    }

    @Transactional
    fun addCartProduct(cartId: Long, productId: Long, userId: Long) {
        val sessionProduct = findSessionProduct(cartId, productId, userId)

        sessionProduct.quantity = checkNotNull(sessionProduct.quantity) + 1
        sessionProductDao.save(sessionProduct)
    }

    @Transactional
    fun deleteCartProduct(cartId: Long, productId: Long, userId: Long) {
        val sessionProduct = findSessionProduct(cartId, productId, userId)

        val productQuantity = checkNotNull(sessionProduct.quantity)
        if (productQuantity == 0) { // Let's guess it's alright
            return
        }
        check(productQuantity > 0)

        sessionProduct.quantity = productQuantity - 1
        sessionProductDao.save(sessionProduct)
    }

    private fun findSessionProduct(cartId: Long, productId: Long, userId: Long): SessionProduct {
        val activeCartSession = shoppingSessionDao.findActiveCartSession(cartId) ?: run {
            throw ServiceException("No active sessions were found for given cart")
        }

        if (checkNotNull(activeCartSession.user).id != userId) {
            throw ServiceException("Shopping session not fund") // it's a lie
        }

        val sessionId = checkNotNull(activeCartSession.id)

        val sessionProduct = sessionProductDao.findByProduct_IdAndSession_Id(productId, sessionId)
        return if (sessionProduct != null) sessionProduct
        else {
            val product = productDao.findById(productId).takeIf { it.isPresent }?.get() ?: run {
                throw ServiceException("Could not find requested product")
            }

            SessionProduct(
                product = product, session = activeCartSession,
                quantity = 0
            )
        }
    }
}