package ru.itmo.model.vo

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.GrantedAuthority

class UserAuthentication : UsernamePasswordAuthenticationToken {

    val userId: Long

    val email: String

    constructor(
        principal: Any, credentials: Any?,
        id: Long, email: String
    ) : super(principal, credentials) {
        this.userId = id
        this.email = email
    }

    constructor(
        principal: Any, credentials: Any?,
        authorities: List<GrantedAuthority>,
        id: Long, email: String
    ) : super(principal, credentials, authorities) {
        this.userId = id
        this.email = email
    }
}