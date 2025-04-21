package com.management.transaction.exception;

import com.management.transaction.dto.ErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(InvalidAccountException.class)
    public ResponseEntity<Object> handleInvalidAccountException(InvalidAccountException ex, WebRequest request) {
        ErrorDTO body = new ErrorDTO(HttpStatus.NOT_FOUND.toString(), ex.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InsufficientFundsException.class)
    public ResponseEntity<ErrorDTO> handleInsufficientFundsException(InsufficientFundsException ex, WebRequest request) {
        ErrorDTO body = new ErrorDTO(HttpStatus.BAD_REQUEST.toString(), ex.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
        ErrorDTO body = new ErrorDTO(HttpStatus.BAD_REQUEST.toString(), ex.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGlobalException(Exception ex, WebRequest request) {
        ErrorDTO body = new ErrorDTO(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "An unexpected error occurred.", LocalDateTime.now());
        // Consider logging the exception details for debugging purpose
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
