package com.bancolombia.training.customers.web;

import com.bancolombia.training.customers.catalog.CustomerNotFoundException;
import org.springframework.boot.webflux.autoconfigure.error.AbstractErrorWebExceptionHandler;
import org.springframework.core.codec.DecodingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.ServerWebInputException;

import java.time.Instant;

@RestControllerAdvice
public class CustomerHandlerError {
    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<ApiError> missing(CustomerNotFoundException exception, ServerWebExchange exchange) {
        return error(HttpStatus.NOT_FOUND, "ACCOUNT_NOT_FOUND", exception.getMessage(), exchange);
    }
    @ExceptionHandler({InvalidRequestException.class, ServerWebInputException.class, DecodingException.class})
    public ResponseEntity<ApiError> badRequest(Exception exception, ServerWebExchange exchange) {
        return error(HttpStatus.BAD_REQUEST, "INVALID_REQUEST", "Solicitud de cuenta invalida", exchange);
    }


    @ExceptionHandler({Exception.class})
    public ResponseEntity<ApiError> handleException(Exception exception, ServerWebExchange exchange) {
        return error(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR", "Error interno del servidor", exchange);
    }

    private ResponseEntity<ApiError> error(HttpStatus status, String code, String message, ServerWebExchange exchange) {
        return ResponseEntity.status(status).body(new ApiError(code, message, status.value(),
                exchange.getAttributeOrDefault(CorrelationFilter.ATTRIBUTE, "not-set"), Instant.now()));
    }
}
