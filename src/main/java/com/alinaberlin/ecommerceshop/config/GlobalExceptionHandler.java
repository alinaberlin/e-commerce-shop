package com.alinaberlin.ecommerceshop.config;

import com.alinaberlin.ecommerceshop.exceptions.ForbiddenException;
import com.alinaberlin.ecommerceshop.exceptions.InsuficientStockException;
import com.alinaberlin.ecommerceshop.exceptions.InvalidIdException;
import com.alinaberlin.ecommerceshop.exceptions.InvalidStateException;
import com.alinaberlin.ecommerceshop.payloads.ErrorMsg;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(InsuficientStockException.class)
    public ResponseEntity<ErrorMsg> handleInsuficientSockException(InsuficientStockException e) {
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ErrorMsg("EXPECTATION_FAILED", "Insufficient stock"));
    }

    @ExceptionHandler(InvalidStateException.class)
    public ResponseEntity<ErrorMsg> handleInvalidStateException(InvalidStateException e) {
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ErrorMsg("EXPECTATION_FAILED", "Invalid state transition"));
    }

    @ExceptionHandler(InvalidIdException.class)
    public ResponseEntity<ErrorMsg> handleInvalidIdException(InvalidIdException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorMsg("NOT_FOUND", "Entity not found"));
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ErrorMsg> handleForbiddenException(ForbiddenException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorMsg("FORBIDDEN", "Missing authorization"));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorMsg> handleConstraintViolationException(ConstraintViolationException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorMsg("CONFLICT", "Duplicate value"));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMsg> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMsg("BAD_REQUEST", e.getMessage()));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorMsg> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorMsg("CONFLICT", "Duplicate value"));
    }
}
