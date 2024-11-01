package com.mprtcz.controllers.exceptionhandlers;

import com.mprtcz.exceptions.TransactionInvalidException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class PaymentExceptionHandler {

    @ExceptionHandler(TransactionInvalidException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(TransactionInvalidException ex) {
        ErrorResponse errorResponse = ErrorResponse.create(ex, HttpStatusCode.valueOf(400), ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
