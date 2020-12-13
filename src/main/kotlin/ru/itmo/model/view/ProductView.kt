package ru.itmo.model.view

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ProductView(
    val id: Long? = null,

    val name: String? = null,

    val quantity: Int? = null
)
