package com.moonspoon.moonspoon.exception;

import com.moonspoon.moonspoon.dto.response.error.DuplicateErrorResponse;
import com.moonspoon.moonspoon.dto.response.error.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(DuplicateUserException.class)
    public ResponseEntity<DuplicateErrorResponse> handleDuplicateUserException(DuplicateUserException ex){
        DuplicateErrorResponse errorResponse = new DuplicateErrorResponse(ex.getMessage(), false);
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(NotUserException.class)
    public ResponseEntity<ErrorResponse> handleNotUserException(NotUserException ex){
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.FORBIDDEN.value(), ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException ex){
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ProblemNotInWorkbook.class)
    public ResponseEntity<ErrorResponse> handleProblemNotInWorkbook(ProblemNotInWorkbook ex){
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

}
