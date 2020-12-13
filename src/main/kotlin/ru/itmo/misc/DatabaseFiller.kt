package ru.itmo.misc

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import ru.itmo.dao.*
import ru.itmo.model.entity.Cart
import ru.itmo.model.entity.Product
import ru.itmo.model.entity.User
import javax.annotation.PostConstruct
import kotlin.random.Random

@Suppress("UNUSED_VARIABLE")
@Component
class DatabaseFiller(
    private val userDao: UserDao,

    private val cartDao: CartDao,
    private val productDao: ProductDao,

    private val shoppingSessionDao: ShoppingSessionDao,
    private val sessionProductDao: SessionProductDao
) {

    private val random: Random = Random(1428)

    @PostConstruct
    fun fillDatabase() {
        val users = (1 until 10)
            .map { index -> User(email = "customer$index@itmo.ru", password = BCRYPTED_PASSWORD) }
            .onEach { userDao.save(it) }
            .onEach { LOGGER.info(it.toString()) }

        val cartUser = userDao.save(User(email = "carts@itmo.ru", password = BCRYPTED_PASSWORD))



        val carts = (1 until 5)
            .map { index -> Cart(secretToken = CART_SECRET_TOKEN + index) }
            .onEach { cartDao.save(it) }
            .onEach { LOGGER.info(it.toString()) }



        val products = (1 until 20)
            .map { index -> Product(name = "Product$index") }
            .onEach { productDao.save(it) }
            .onEach { LOGGER.info(it.toString()) }
    }

    companion object {
        private val LOGGER: Logger = LoggerFactory.getLogger(DatabaseFiller::class.java)

        // Decrypted version: 'helloworld'
        // https://bcrypt-generator.com/
        private const val BCRYPTED_PASSWORD = "\$2y\$12\$mWKQrf0RTiEGI5dXEaAKjOrOj.GKTrDx6Q06yG/f24Vt8v9mKi8n."

        private const val CART_SECRET_TOKEN = "top_secret"
    }
}