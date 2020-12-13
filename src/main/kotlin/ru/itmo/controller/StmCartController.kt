package ru.itmo.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ru.itmo.controller.model.request.AddCartProductRequest
import ru.itmo.service.StmCartService

@RestController
@RequestMapping(path = ["/carts"])
class StmCartController {

    @Autowired
    internal lateinit var cartService: StmCartService

    @PostMapping(
        path = ["/{cartId}/add-product"],
        headers = ["$STM_INDICATOR_HEADER=1", HttpHeaders.AUTHORIZATION]
    )
    fun addCartProduct(
        @PathVariable("cartId") cardId: Long,
        @RequestBody request: AddCartProductRequest,

        @RequestHeader(HttpHeaders.AUTHORIZATION) authorizationHeader: String
    ): ResponseEntity<Any> {
        if (authorizationHeader.indexOf(' ') == -1) {
            return ResponseEntity.notFound().build()
        }
        val secretToken = authorizationHeader.split(' ')[1]

        cartService.addCartProduct(cardId, request.productId, secretToken)

        return ResponseEntity.ok().build()
    }

    companion object {
        const val STM_INDICATOR_HEADER = "STM-CONTEXT"
    }
}