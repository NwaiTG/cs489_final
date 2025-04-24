package com.nwai.dentalsys.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice // global error handling
public class GlobalExceptionHandler {
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> handleDataIntegrityViolationException(DataIntegrityViolationException e, HttpServletRequest request) {
        ApiError errorMsg = new ApiError(
                e.getMessage(),
                request.getRequestURI(),
                HttpStatus.CONFLICT.value(),
                Instant.now()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorMsg);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleResourceNotFoundException(ResourceNotFoundException e, HttpServletRequest request) {
        ApiError errorMsg = new ApiError(
                e.getMessage(),
                request.getRequestURI(),
                HttpStatus.NOT_FOUND.value(),
                Instant.now()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMsg);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleMethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request) {
        String errMessage = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        ApiError errorMsg = new ApiError(
                errMessage,
                request.getRequestURI(),
                HttpStatus.BAD_REQUEST.value(),
                Instant.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMsg);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> handleHttpMessageNotReadableException(HttpMessageNotReadableException e, HttpServletRequest request) {
        ApiError errorMsg = new ApiError(
                e.getMessage(),
                request.getRequestURI(),
                HttpStatus.BAD_REQUEST.value(),
                Instant.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMsg);
    }

    @ExceptionHandler(DataAlreadyExistedException.class)
    public ResponseEntity<ApiError> handleDataAlreadyExistedException(DataAlreadyExistedException e, HttpServletRequest request) {
        ApiError errorMsg = new ApiError(
                e.getMessage(),
                request.getRequestURI(),
                HttpStatus.CONTINUE.value(),
                Instant.now()
        ) ;
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorMsg);
    }

    @ExceptionHandler(LimitExceededException.class)
    public ResponseEntity<ApiError> handleLimitExceededException(LimitExceededException e, HttpServletRequest request) {
        ApiError errorMsg = new ApiError(
                e.getMessage(),
                request.getRequestURI(),
                HttpStatus.FORBIDDEN.value(),
                Instant.now()
        );
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorMsg);
    }
}
