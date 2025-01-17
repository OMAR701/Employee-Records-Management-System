package com.omar.backend.exceptions;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.security.access.AccessDeniedException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @Value("${messages.error.employee.notfound}")
    private String employeeNotFoundMessage;

    @Value("${messages.error.access.denied}")
    private String accessDeniedMessage;

    @ExceptionHandler(EmployeeNotFoundException.class)
    public ResponseEntity<String> handleEmployeeNotFound(EmployeeNotFoundException ex) {
        String errorMessage = employeeNotFoundMessage.replace("{0}", ex.getEmployeeId().toString());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
    }


    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> handleAccessDenied(AccessDeniedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied: You lack necessary permissions.");
    }

}

