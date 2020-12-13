package ru.itmo.service

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import ru.itmo.dao.CartDao
import ru.itmo.dao.ProductDao
import ru.itmo.dao.SessionProductDao
import ru.itmo.dao.ShoppingSessionDao
import ru.itmo.model.entity.SessionProduct
import ru.itmo.model.exception.ServiceException
import javax.transaction.Transactional

@Service
class StmCartService(
    private val cartDao: CartDao,
    private val productDao: ProductDao,
    private val shoppingSessionDao: ShoppingSessionDao,
    private val sessionProductDao: SessionProductDao
) {

    @Transactional
    fun addCartProduct(cartId: Long, productId: Long, secretToken: String) {
        val cart = cartDao.findByIdOrNull(cartId) ?: return
        if (secretToken != cart.secretToken) {
            LOGGER.warn("Got wrong STM secret token \"{}\" for cart #{}", secretToken, cartId)
            return
        }

        val activeCartSession = shoppingSessionDao.findActiveCartSession(cartId) ?: run {
            return@addCartProduct // nothing to do
        }

        val sessionId = checkNotNull(activeCartSession.id)
        val sessionProduct = sessionProductDao.findByProduct_IdAndSession_Id(productId, sessionId).let { sessionProduct ->
            if (sessionProduct != null) sessionProduct
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

        sessionProduct.quantity = checkNotNull(sessionProduct.quantity) + 1
        sessionProductDao.save(sessionProduct)
    }

    companion object {
        private val LOGGER: Logger = LoggerFactory.getLogger(StmCartService::class.java)
    }
}