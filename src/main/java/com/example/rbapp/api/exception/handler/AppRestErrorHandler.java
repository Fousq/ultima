package com.example.rbapp.api.exception.handler;

import com.example.rbapp.api.exception.NotFoundException;
import com.example.rbapp.api.model.ErrorResponse;
import com.example.rbapp.timepackage.exception.DuplicateTimePackageException;
import com.example.rbapp.user.exception.UserPasswordNotMatchedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class AppRestErrorHandler {

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleAny(Exception e) {
        log.error("Error: {}", e.getMessage(), e);
        HttpStatus internalServerError = HttpStatus.INTERNAL_SERVER_ERROR;
        ErrorResponse errorResponse = new ErrorResponse(internalServerError.value(), internalServerError.getReasonPhrase());

        return ResponseEntity.internalServerError().body(errorResponse);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException e) {
        log.error("Not found entity error: {}", e.getMessage(), e);
        HttpStatus notFound = HttpStatus.NOT_FOUND;
        ErrorResponse errorResponse = new ErrorResponse(notFound.value(), notFound.getReasonPhrase());
        return ResponseEntity.status(notFound).body(errorResponse);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleDuplicateTimePackage(DuplicateTimePackageException e) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Time package cannot be duplicated");
        return ResponseEntity.internalServerError().body(errorResponse);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleUserPasswordNotMatchedException(UserPasswordNotMatchedException e) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "User password doesn't match");
        return ResponseEntity.internalServerError().body(errorResponse);
    }
}
