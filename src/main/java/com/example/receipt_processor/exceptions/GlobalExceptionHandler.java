package com.example.receipt_processor.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Handles catching exceptions thrown from the controller
 * Controller should be responsible only with handle incoming requests and sending back a response back to a client
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles invalid receipts being sent from a client
     * MethodArgumentNotValidException is an exception thrown when @Valid is doing its check and sees a property that is not valid
     *
     * @return error response
     */
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<String> handleMethodArgException(MethodArgumentNotValidException exception) {
//        List<FieldError> errors = exception.getFieldErrors();
//        for(FieldError er: errors){
//            System.out.println(er.getField());
//            System.out.println(er.getDefaultMessage());
//        }
//        return ResponseEntity.status(400).body("Invalid Receipt");
//    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDTO> handleMethodArgException(MethodArgumentNotValidException exception) {
        List<Map<String, String>> errors = new ArrayList<>();
        List<FieldError> fieldErrors = exception.getFieldErrors();
        for(FieldError er: fieldErrors){
            Map<String, String> errorMsg = new HashMap<>();
            errorMsg.put(er.getField(), er.getDefaultMessage());
            errors.add(errorMsg);
        }
        ErrorDTO errorDTO = new ErrorDTO("Invalid Receipt", errors);
        return ResponseEntity.status(400).body(errorDTO);
    }

    /**
     * Handle when user attempts to find an invalid id that is not in the format of UUID
     *
     * @return error response
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgException() {
        return ResponseEntity.status(404).body("No receipt found for that id");
    }

    @ExceptionHandler(NoIdFoundException.class)
    public ResponseEntity<String> handleNoIdFoundException(){
        return ResponseEntity.status(404).body("No receipt found for that id");
    }
}
