package ru.itmo.autoconfigure.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "jwt")
class JwtProperties {
    lateinit var secret: String

    lateinit var type: String

    lateinit var issuer: String

    lateinit var audience: String
}