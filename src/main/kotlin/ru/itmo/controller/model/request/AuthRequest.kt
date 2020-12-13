package ru.itmo.controller.model.request

data class AuthRequest(
    val email: String,
    val password: String
)