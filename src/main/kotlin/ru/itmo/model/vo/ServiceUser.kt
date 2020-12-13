package ru.itmo.model.vo

import org.springframework.security.core.userdetails.User

data class ServiceUser(
    var id: Long? = null,

    var email: String? = null,

    private var password: String? = null
) : User(email, password, emptyList()) {

    override fun getPassword(): String?  = password

    fun setPassword(password: String) {
        this.password = password
    }
}