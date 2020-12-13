package ru.itmo.controller.advice

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.DisabledException
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest

@ControllerAdvice
class AuthenticationAdvice {

    @ExceptionHandler(DisabledException::class)
    fun handleDisabledException(ex: DisabledException, request: WebRequest): ResponseEntity<ErrorResponse> {
        LOGGER.debug("", ex)
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorResponse("AUTH_DISABLED", ex.message))
    }

    @ExceptionHandler(BadCredentialsException::class)
    fun handleBadCredentialsException(ex: BadCredentialsException, request: WebRequest): ResponseEntity<ErrorResponse> {
            LOGGER.debug("", ex)
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorResponse("AUTH_CREDS", ex.message))
    }

    @ExceptionHandler(UsernameNotFoundException::class)
    fun handleUserNotFoundException(ex: UsernameNotFoundException, request: WebRequest): ResponseEntity<ErrorResponse> {
        LOGGER.debug("", ex)
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorResponse("AUTH_CREDS", ex.message))
    }

    companion object {
        private val LOGGER: Logger = LoggerFactory.getLogger(AuthenticationAdvice::class.java)
    }
}