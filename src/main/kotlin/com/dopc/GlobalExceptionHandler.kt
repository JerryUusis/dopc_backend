package com.dopc

import com.dopc.exception.InvalidCoordinatesException
import org.springframework.http.HttpStatus
import org.springframework.web.ErrorResponse
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class GlobalExceptionHandler {
    // Use RFC 9457 standard for error responses
    // https://datatracker.ietf.org/doc/html/rfc9457
    @ExceptionHandler(InvalidCoordinatesException::class)
    fun handleInvalidCoordinatesException(exception: InvalidCoordinatesException): ErrorResponse {
        return ErrorResponse.builder(exception, HttpStatus.BAD_REQUEST, exception.message ?: "invalid coordinate")
            .build()
    }
}