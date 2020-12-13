package ru.itmo.controller.advice

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component
import java.io.Serializable
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class JwtAuthenticationEntryPoint : AuthenticationEntryPoint, Serializable {
    override fun commence(
        request: HttpServletRequest, response: HttpServletResponse,
        authException: AuthenticationException
    ) {
        LOGGER.debug("", authException)
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.message)
    }

    companion object {
        private val LOGGER: Logger = LoggerFactory.getLogger(JwtAuthenticationEntryPoint::class.java)
    }
}