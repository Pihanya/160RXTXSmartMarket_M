package ru.itmo.controller.auth

import io.jsonwebtoken.ExpiredJwtException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import ru.itmo.autoconfigure.properties.JwtProperties
import ru.itmo.controller.StmCartController
import ru.itmo.model.vo.UserAuthentication
import ru.itmo.service.JwtUserDetailsService
import ru.itmo.util.JwtTokenUtils
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@Component
class JwtAuthTokenFilter(
    jwtProperties: JwtProperties,
    private val userDetailsService: JwtUserDetailsService
) : OncePerRequestFilter() {

    private val jwtTokenUtil: JwtTokenUtils = JwtTokenUtils(jwtProperties)

    override fun doFilterInternal(
        request: HttpServletRequest, response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        if (request.getHeader(StmCartController.STM_INDICATOR_HEADER) == "1") {
            filterChain.doFilter(request, response)
            return
        }

        val authorizationHeader = request.getHeader("Authorization")

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer")) {
            val jwtToken = authorizationHeader.substring(7)

            val username = try {
                jwtTokenUtil.getUsernameFromToken(jwtToken)
            } catch (ex: IllegalArgumentException) {
                LOGGER.warn("", ex)
                null
            } catch (ex: ExpiredJwtException) {
                LOGGER.warn("", ex)
                null
            }

            if (username != null && SecurityContextHolder.getContext().authentication == null) {
                val serviceUser = userDetailsService.loadUserByUsername(username)
                if (jwtTokenUtil.validateToken(jwtToken, serviceUser)) {
                    val userAuthentication = UserAuthentication(
                        serviceUser, null,
                        emptyList(),
                        checkNotNull(serviceUser.id), username
                    )
                    val authToken = UsernamePasswordAuthenticationToken(userAuthentication, null, emptyList())
                    authToken.details = WebAuthenticationDetailsSource().buildDetails(request)
                    // After setting the Authentication in the context, we specify
                    // that the current user is authenticated. So it passes the Spring Security Configurations successfully.
                    // After setting the Authentication in the context, we specify
                    // that the current user is authenticated. So it passes the Spring Security Configurations successfully.
                    SecurityContextHolder.getContext().authentication = authToken
                }
            }
        }

        filterChain.doFilter(request, response)
    }

    companion object {
        private val LOGGER: Logger = LoggerFactory.getLogger(JwtAuthTokenFilter::class.java)
    }
}