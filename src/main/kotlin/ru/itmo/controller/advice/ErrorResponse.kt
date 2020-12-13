package ru.itmo.controller.advice

data class ErrorResponse(
    val errorCode: String,
    val message: String? = null
)