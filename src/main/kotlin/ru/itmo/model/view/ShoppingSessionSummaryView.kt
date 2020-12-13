package ru.itmo.model.view

import com.fasterxml.jackson.annotation.JsonInclude
import java.time.LocalDateTime

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ShoppingSessionSummaryView(
    val cartId: Long? = null,

    val startedDate: LocalDateTime? = null
)