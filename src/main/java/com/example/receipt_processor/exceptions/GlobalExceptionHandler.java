package com.example.receipt_processor.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Handles catching exceptions thrown from the controller
 * Controller should be responsible only with handle incoming requests and sending back a response back to a client
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles invalid receipts being sent from a client
     * @return error response
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleMethodArgException(){
        return ResponseEntity.status(400).body("Invalid Receipt");
    }

    /**
     * Handle when user attempts to find an invalid id that is not in the format of UUID
     * @return error response
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgException(){
        return ResponseEntity.status(404).body("No receipt found for that id");
    }
}
