package ru.itmo.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import ru.itmo.controller.model.request.AddCartProductRequest
import ru.itmo.controller.model.request.DeleteCartProductRequest
import ru.itmo.model.view.CartStateView
import ru.itmo.model.vo.UserAuthentication
import ru.itmo.service.CartService
import ru.itmo.service.ShoppingSessionService

@RestController
@RequestMapping(path = ["/carts"])
class CartController {

    @Autowired
    internal lateinit var shoppingSessionService: ShoppingSessionService

    @Autowired
    internal lateinit var cartService: CartService

    @GetMapping("/{cartId}")
    fun getCartState(
        @PathVariable("cartId") cardId: Long,
        @AuthenticationPrincipal authentication: UserAuthentication
    ): ResponseEntity<CartStateView> {
        val cartState = cartService.getCartState(cardId, authentication.userId)
        return ResponseEntity.ok(cartState)
    }

    @PostMapping(path = ["/{cartId}/hold"])
    fun holdCart(
        @PathVariable("cartId") cardId: Long,
        @AuthenticationPrincipal authentication: UserAuthentication
    ): ResponseEntity<Any> {
        shoppingSessionService.startSession(cardId, authentication.userId)
        return ResponseEntity.ok().build()
    }

    @PostMapping(path = ["/{cartId}/buy"])
    fun buyCartProducts(
        @PathVariable("cartId") cardId: Long,
        @AuthenticationPrincipal authentication: UserAuthentication
    ): ResponseEntity<Any> {
        shoppingSessionService.endSession(authentication.userId, cardId)
        return ResponseEntity.ok().build()
    }

    @PostMapping(path = ["/{cartId}/release"])
    fun releaseCart(
        @PathVariable("cartId") cardId: Long,
        @AuthenticationPrincipal authentication: UserAuthentication
    ): ResponseEntity<Any> {
        val releasedSession = shoppingSessionService.releaseSession(authentication.userId, cardId)

        return if (releasedSession == null) ResponseEntity.notFound().build()
        else ResponseEntity.ok().build()
    }

    @PostMapping(path = ["/{cartId}/add-product"])
    fun addCartProduct(
        @PathVariable("cartId") cardId: Long,
        @RequestBody request: AddCartProductRequest,
        @AuthenticationPrincipal authentication: UserAuthentication
    ): ResponseEntity<Any> {
        cartService.addCartProduct(cardId, request.productId, authentication.userId)
        return ResponseEntity.ok().build()
    }

    @DeleteMapping(path = ["/{cartId}/delete-product"])
    fun deleteCartProduct(
        @PathVariable("cartId") cardId: Long,
        @RequestBody request: DeleteCartProductRequest,
        @AuthenticationPrincipal authentication: UserAuthentication
    ): ResponseEntity<Any> {
        cartService.deleteCartProduct(cardId, request.productId, authentication.userId)
        return ResponseEntity.ok().build()
    }
}