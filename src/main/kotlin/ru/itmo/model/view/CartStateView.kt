package ru.itmo.model.view

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class CartStateView(
    val cartId: Long? = null,

    val products: List<ProductView>? = emptyList(),

    val state: CartState? = null
) {

    enum class CartState {
        FREE, BUSY, OWNED;
    }
}
