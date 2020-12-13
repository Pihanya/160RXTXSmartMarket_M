package ru.itmo.controller.advice

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import ru.itmo.model.exception.ServiceException

@ControllerAdvice
class ServiceAdvice {

    @ExceptionHandler(ServiceException::class)
    fun handleServiceException(ex: ServiceException, request: WebRequest): ResponseEntity<ErrorResponse> {
        LOGGER.warn("", ex)
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponse("SERVICE", ex.message))
    }

    companion object {
        private val LOGGER: Logger = LoggerFactory.getLogger(ServiceAdvice::class.java)
    }
}